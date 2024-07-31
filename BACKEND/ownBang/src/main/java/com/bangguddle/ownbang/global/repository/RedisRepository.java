package com.bangguddle.ownbang.global.repository;

import com.bangguddle.ownbang.global.dto.Tokens;
import com.bangguddle.ownbang.global.handler.AppException;

import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.TOKEN_INVALID;


public interface RedisRepository {
    void save(final Tokens token);

    void delete(final String accessToken);

    Optional<String> findByToken(final String accessToken);

    void saveValidTokens(final Tokens tokens, final long id);

    void deleteValidTokens(final long id);

    Optional<Tokens> findValidTokens(final long id);

    default String getByToken(final String accessToken) {
        return findByToken(accessToken)
                .orElseThrow(() -> new AppException(TOKEN_INVALID));
    }

    default Tokens getValidTokens(final long id) {
        return findValidTokens(id)
                .orElseThrow(() -> new AppException(TOKEN_INVALID));
    }
}
