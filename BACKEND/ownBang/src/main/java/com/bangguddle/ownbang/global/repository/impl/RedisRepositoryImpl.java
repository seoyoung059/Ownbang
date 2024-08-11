package com.bangguddle.ownbang.global.repository.impl;

import com.bangguddle.ownbang.global.dto.Tokens;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.bangguddle.ownbang.global.enums.ErrorCode.TOKEN_INVALID;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {
    private static final Long TIME_TO_LIVE = 60L * 60 * 24 * 7;
    private static final String AUTH_TOKEN_PREFIX = "TOKENS_";
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(final Tokens token) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(token.accessToken(), token.refreshToken());
        redisTemplate.expire(token.accessToken(), TIME_TO_LIVE, TimeUnit.SECONDS);
    }

    @Override
    public void delete(final String accessToken) {
        if (Boolean.FALSE.equals(redisTemplate.delete(accessToken)))
            throw new AppException(TOKEN_INVALID);
    }

    @Override
    public Optional<String> findByToken(final String token) {
        String refreshToken = redisTemplate.opsForValue().get(token);
        if (Objects.isNull(refreshToken)) return Optional.empty();
        return Optional.of(refreshToken);
    }

    @Override
    public void saveValidTokens(Tokens tokens, long id) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        String key = getCurrentTokenKey(id);

        hashOperations.put(key, "access_token", tokens.accessToken());
        hashOperations.put(key, "refresh_token", tokens.refreshToken());
        redisTemplate.expire(key, TIME_TO_LIVE, TimeUnit.SECONDS);
    }

    @Override
    public void deleteValidTokens(long id) {
        String key = getCurrentTokenKey(id);
        if (Boolean.FALSE.equals(redisTemplate.delete(key)))
            throw new AppException(TOKEN_INVALID);
    }

    @Override
    public Optional<Tokens> findValidTokens(long id) {
        String key = getCurrentTokenKey(id);
        String accessToken = (String) redisTemplate.opsForHash().get(key, "access_token");
        String refreshToken = (String) redisTemplate.opsForHash().get(key, "refresh_token");

        if (Objects.isNull(accessToken)) return Optional.empty();

        return Optional.ofNullable(
                Tokens.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );
    }

    private String getCurrentTokenKey(long id) {
        return String.format("%s%d", AUTH_TOKEN_PREFIX, id);
    }
}
