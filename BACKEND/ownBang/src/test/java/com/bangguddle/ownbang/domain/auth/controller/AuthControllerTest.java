package com.bangguddle.ownbang.domain.auth.controller;

import com.bangguddle.ownbang.domain.auth.dto.DuplicateResponse;
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

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthService authService;

    private UserSignUpRequest makeSignUpRequest(String name) {
        return UserSignUpRequest.builder()
                .name(name)
                .build();
    }

    @Test
    @DisplayName("회원 가입 성공")
    public void 회원_가입_성공() {
        // given
        UserSignUpRequest newUserRequest = makeSignUpRequest("new");
        SuccessResponse<NoneResponse> success =
                new SuccessResponse<>(SIGNUP_SUCCESS, NoneResponse.NONE);

        // when
        when(authService.signUp(newUserRequest)).thenReturn(success);

        // then
        assertThatCode(() -> authController.signUp(newUserRequest)).doesNotThrowAnyException();
        assertThat(authController.signUp(newUserRequest).getStatusCode()).isEqualTo(SIGNUP_SUCCESS.getHttpStatus());
        assertThat(authController.signUp(newUserRequest)).isInstanceOf(ResponseEntity.class)
                .isEqualTo(Response.success(success));
    }

    @Test
    @DisplayName("회원 가입 실패 - 전화번호 중복")
    public void 회원_가입_실패_전화번호_중복() {
        // given
        UserSignUpRequest pExistUserRequest = makeSignUpRequest("phone");

        // when
        doThrow(new AppException(PHONE_NUMBER_DUPLICATED))
                .when(authService).signUp(pExistUserRequest);

        // then
        assertThatThrownBy(() -> authController.signUp(pExistUserRequest))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(PHONE_NUMBER_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("회원 가입 실패 - 이메일 중복")
    public void 회원_가입_실패_이메일_중복() {
        // given
        UserSignUpRequest eExistUserRequest = makeSignUpRequest("email");

        // when
        doThrow(new AppException(EMAIL_DUPLICATED))
                .when(authService).signUp(eExistUserRequest);

        // then
        assertThatThrownBy(() -> authService.signUp(eExistUserRequest))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(EMAIL_DUPLICATED.getMessage());

    }

    @Test
    @DisplayName("이메일 중복 확인 성공 - 중복되지 않음")
    public void 이메일_중복_확인_성공_중복되지_않음() {
        // given
        String email = "isNotDuplicate";
        DuplicateResponse response = new DuplicateResponse(false);
        SuccessResponse<DuplicateResponse> success =
                new SuccessResponse<>(CHECK_EMAIL_DUPLICATE_SUCCESS, response);

        // when
        when(authService.checkEmailDuplicate(email)).thenReturn(success);

        // then
        assertThatCode(() -> authController.checkEmailDuplicate(email)).doesNotThrowAnyException();
        assertThat(authController.checkEmailDuplicate(email).getStatusCode())
                .isEqualTo(CHECK_EMAIL_DUPLICATE_SUCCESS.getHttpStatus());
        assertThat(authController.checkEmailDuplicate(email)).isInstanceOf(ResponseEntity.class)
                .isEqualTo(Response.success(success));
    }

    @Test
    @DisplayName("이메일 중복 확인 성공 - 중복됨")
    public void 이메일_중복_확인_성공_중복됨() {
        // given
        String email = "isDuplicate";
        DuplicateResponse response = new DuplicateResponse(true);
        SuccessResponse<DuplicateResponse> success =
                new SuccessResponse<>(CHECK_EMAIL_DUPLICATE_SUCCESS, response);

        // when
        when(authService.checkEmailDuplicate(email)).thenReturn(success);

        // then
        assertThatCode(() -> authController.checkEmailDuplicate(email)).doesNotThrowAnyException();
        assertThat(authController.checkEmailDuplicate(email).getStatusCode())
                .isEqualTo(CHECK_EMAIL_DUPLICATE_SUCCESS.getHttpStatus());
        assertThat(authController.checkEmailDuplicate(email)).isInstanceOf(ResponseEntity.class)
                .isEqualTo(Response.success(success));
    }


}