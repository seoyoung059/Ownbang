package com.bangguddle.ownbang.domain.auth.service;

import com.bangguddle.ownbang.domain.auth.dto.UserSignUpRequest;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface AuthService {
    SuccessResponse<NoneResponse> signUp(UserSignUpRequest request);
}