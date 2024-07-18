package com.bangguddle.ownbang.domain.auth.service;

import com.bangguddle.ownbang.domain.auth.dto.UserSignUpRequest;
import com.bangguddle.ownbang.global.response.MessageResponse;

public interface AuthService {
    MessageResponse signUp(UserSignUpRequest request);
}
