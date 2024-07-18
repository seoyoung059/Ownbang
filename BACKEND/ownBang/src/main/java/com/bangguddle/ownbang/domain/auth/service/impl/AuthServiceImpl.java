package com.bangguddle.ownbang.domain.auth.service.impl;

import com.bangguddle.ownbang.domain.auth.dto.UserSignUpRequest;
import com.bangguddle.ownbang.domain.auth.service.AuthService;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    @Override
    public MessageResponse signUp(UserSignUpRequest request) {
        if(userRepository.findByEmail(request.email()).isPresent())
            throw new AppException(ErrorCode.EMAIL_DUPLICATED);
        User user = request.toEntity();
        userRepository.save(user);
        return new MessageResponse(SuccessCode.SIGNUP_SUCCESS.getMessage());
    }
}
