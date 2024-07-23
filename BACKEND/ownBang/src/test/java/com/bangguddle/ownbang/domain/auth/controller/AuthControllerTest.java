package com.bangguddle.ownbang.domain.auth.controller;

import com.bangguddle.ownbang.domain.auth.dto.UserSignUpRequest;
import com.bangguddle.ownbang.domain.auth.service.AuthService;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthService authService;

    @Test
    @DisplayName("회원 가입 성공")
    public void 회원_가입_성공() {
        // given
        UserSignUpRequest newUserRequest = makeRequest("new");

        // when
        SuccessResponse<NoneResponse> success =
                new SuccessResponse<>(SuccessCode.SIGNUP_SUCCESS, NoneResponse.NONE);
        when(authService.signUp(newUserRequest)).thenReturn(success);

        // then
        assertThatCode(() -> authController.signUp(newUserRequest)).doesNotThrowAnyException();
        assertThat(authController.signUp(newUserRequest).getStatusCode()).isEqualTo(SuccessCode.SIGNUP_SUCCESS.getHttpStatus());
        assertThat(authController.signUp(newUserRequest)).isInstanceOf(ResponseEntity.class)
                .isEqualTo(Response.success(success));
    }

    @Test
    @DisplayName("회원 가입 실패 - 전화번호 중복")
    public void 회원_가입_실패_전화번호_중복() {
        // given
        UserSignUpRequest pExistUserRequest = makeRequest("phone");

        // when
        doThrow(new AppException(ErrorCode.PHONE_NUMBER_DUPLICATED))
                .when(authService).signUp(pExistUserRequest);

        // then
        assertThatThrownBy(() -> authController.signUp(pExistUserRequest))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.PHONE_NUMBER_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("회원 가입 실패 - ")
    public void 회원_가입_실패_이메일_중복() {
        // given
        UserSignUpRequest eExistUserRequest = makeRequest("email");

        // when
        doThrow(new AppException(ErrorCode.EMAIL_DUPLICATED))
                .when(authService).signUp(eExistUserRequest);

        // then
        assertThatThrownBy(() -> authService.signUp(eExistUserRequest))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.EMAIL_DUPLICATED.getMessage());

    }

    private UserSignUpRequest makeRequest(String name) {
        return UserSignUpRequest.builder()
                .name(name)
                .build();
    }
}