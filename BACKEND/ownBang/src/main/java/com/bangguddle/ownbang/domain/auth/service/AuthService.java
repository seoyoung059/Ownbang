package com.bangguddle.ownbang.domain.auth.service;

import com.bangguddle.ownbang.domain.auth.dto.*;
import com.bangguddle.ownbang.global.dto.Tokens;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface AuthService {
    SuccessResponse<NoneResponse> signUp(UserSignUpRequest request);
    SuccessResponse<DuplicateResponse> checkEmailDuplicate(String email);
    SuccessResponse<DuplicateResponse> checkPhoneNumberDuplicate(String phoneNumber);
    SuccessResponse<Tokens> login(LoginRequest request);
    SuccessResponse<PasswordCheckResponse> passwordCheck(Long id, PasswordCheckRequest request);
    SuccessResponse<NoneResponse> logout(String header, Long id);
    SuccessResponse<NoneResponse> passwordChange(Long id, PasswordChangeRequest request);
}
