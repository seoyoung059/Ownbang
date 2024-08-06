package com.bangguddle.ownbang.domain.auth.service.impl;

import com.bangguddle.ownbang.domain.auth.dto.*;
import com.bangguddle.ownbang.global.dto.Tokens;
import com.bangguddle.ownbang.domain.auth.service.AuthService;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.config.security.JwtProvider;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.repository.RedisRepository;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.bangguddle.ownbang.global.enums.ErrorCode.EMAIL_DUPLICATED;
import static com.bangguddle.ownbang.global.enums.ErrorCode.PHONE_NUMBER_DUPLICATED;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final int TOKEN_SPLIT_INDEX = 7;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RedisRepository redisRepository;
    @Override
    public SuccessResponse<NoneResponse> signUp(UserSignUpRequest request) {
        validateByEmail(request.email());
        validateByPhoneNumber(request.phoneNumber());
        User user = request.toEntity(passwordEncoder.encode(request.password()));

        userRepository.save(user);
        return new SuccessResponse<>(SIGNUP_SUCCESS, NoneResponse.NONE);
    }

    @Override
    public SuccessResponse<DuplicateResponse> checkEmailDuplicate(String email) {
        Boolean isDuplicated = userRepository.findByEmail(email).isPresent();
        DuplicateResponse response = new DuplicateResponse(isDuplicated);
        return new SuccessResponse<>(CHECK_EMAIL_DUPLICATE_SUCCESS, response);
    }

    @Override
    public SuccessResponse<DuplicateResponse> checkPhoneNumberDuplicate(String phoneNumber) {
        Boolean isDuplicated = userRepository.findByPhoneNumber(phoneNumber).isPresent();
        DuplicateResponse response = new DuplicateResponse(isDuplicated);
        return new SuccessResponse<>(CHECK_PHONE_NUMBER_DUPLICATE_SUCCESS, response);
    }

    @Override
    public SuccessResponse<Tokens> login(LoginRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.email(), request.password()
        );
        authenticationManager.authenticate(token);
        Long userId = getByEmail(request.email()).getId();
        Tokens tokens = jwtProvider.generateTokens(userId);
        redisRepository.save(tokens);
        redisRepository.saveValidTokens(tokens, userId);
        return new SuccessResponse<>(LOGIN_SUCCESS, tokens);
    }

    @Override
    public SuccessResponse<PasswordCheckResponse> passwordCheck(Long id, PasswordCheckRequest request) {
        User user = userRepository.getById(id);
        boolean isCorrect = passwordEncoder.matches(request.password(), user.getPassword());
        PasswordCheckResponse response = new PasswordCheckResponse(isCorrect);
        return new SuccessResponse<>(PASSWORD_CHECK_SUCCESS, response);
    }

    @Override
    public SuccessResponse<NoneResponse> logout(String header, Long id) {
        String accessToken = header.substring(TOKEN_SPLIT_INDEX);
        redisRepository.delete(accessToken);
        redisRepository.deleteValidTokens(id);
        return new SuccessResponse<>(LOGOUT_SUCCESS, NoneResponse.NONE);
    }


    private void validateByEmail(final String email) {
        if (userRepository.findByEmail(email).isPresent())
            throw new AppException(EMAIL_DUPLICATED);
    }

    private void validateByPhoneNumber(final String phoneNumber) {
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent())
            throw new AppException(PHONE_NUMBER_DUPLICATED);
    }

    private User getByEmail(final String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(EMAIL_DUPLICATED));
    }
}
