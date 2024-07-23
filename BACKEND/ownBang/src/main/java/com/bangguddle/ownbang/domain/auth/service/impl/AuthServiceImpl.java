package com.bangguddle.ownbang.domain.auth.service.impl;

import com.bangguddle.ownbang.domain.auth.dto.UserSignUpRequest;
import com.bangguddle.ownbang.domain.auth.service.AuthService;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public SuccessResponse<NoneResponse> signUp(UserSignUpRequest request) {
        userRepository.validateByEmail(request.email());
        userRepository.validateByPhoneNumber(request.phoneNumber());
        User user = request.toEntity(passwordEncoder.encode(request.password()));

        userRepository.save(user);
        return new SuccessResponse<>(SuccessCode.SIGNUP_SUCCESS, NoneResponse.NONE);
    }
}
