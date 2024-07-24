package com.bangguddle.ownbang.domain.auth.service;

import com.bangguddle.ownbang.domain.auth.dto.DuplicateResponse;
import com.bangguddle.ownbang.domain.auth.dto.UserSignUpRequest;
import com.bangguddle.ownbang.domain.auth.service.impl.AuthServiceImpl;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.SuccessCode.CHECK_EMAIL_DUPLICATE_SUCCESS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 가입 성공")
    public void 회원_가입_성공() {
        // given
        UserSignUpRequest request = UserSignUpRequest.builder()
                .password("password")
                .email("email")
                .nickname("nickname")
                .phoneNumber("phoneNumber")
                .name("name")
                .build();

        SuccessResponse<NoneResponse> expected =
                new SuccessResponse<>(SuccessCode.SIGNUP_SUCCESS, NoneResponse.NONE);

        // when
        doNothing().when(userRepository).validateByEmail(request.email());
        doNothing().when(userRepository).validateByPhoneNumber(request.phoneNumber());

        // then
        assertThatCode(() -> authService.signUp(request)).doesNotThrowAnyException();
        assertThat(authService.signUp(request)).isInstanceOf(SuccessResponse.class)
                .isEqualTo(expected);

    }

    @Test
    @DisplayName("회원 가입 실패 - 전화번호 중복")
    public void 회원_가입_실패_전화번호_중복() {
        // given
        UserSignUpRequest request = UserSignUpRequest.builder()
                .phoneNumber("phoneNumber")
                .build();

        // when
        doThrow(new AppException(ErrorCode.PHONE_NUMBER_DUPLICATED))
                .when(userRepository).validateByPhoneNumber(request.phoneNumber());

        // then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.PHONE_NUMBER_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("회원 가입 실패 - 이메일 중복")
    public void 회원_가입_실패_이메일_중복() {
        // given
        UserSignUpRequest request = UserSignUpRequest.builder()
                .email("email")
                .build();

        // when
        doThrow(new AppException(ErrorCode.EMAIL_DUPLICATED))
                .when(userRepository).validateByEmail(request.email());

        // then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.EMAIL_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("이메일 중복 확인 성공 - 중복됨")
    public void 이메일_중복_확인_성공_중복됨() {
        // given
        String email = "isDuplicate";
        DuplicateResponse response = new DuplicateResponse(true);
        SuccessResponse<DuplicateResponse> success =
                new SuccessResponse<>(CHECK_EMAIL_DUPLICATE_SUCCESS, response);
        User user = User.builder().email(email).build();

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // then
        assertThatCode(() -> authService.checkEmailDuplicate(email)).doesNotThrowAnyException();
        assertThat(authService.checkEmailDuplicate(email)).isInstanceOf(SuccessResponse.class)
                .isEqualTo(success);
    }

    @Test
    @DisplayName("이메일 중복 확인 성공 - 중복되지 않음")
    public void 이메일_중복_확인_성공_중복되지않음() {
        // given
        String email = "isNotDuplicate";
        DuplicateResponse response = new DuplicateResponse(false);
        SuccessResponse<DuplicateResponse> success =
                new SuccessResponse<>(CHECK_EMAIL_DUPLICATE_SUCCESS, response);
        User user = User.builder().email(email).build();

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // then
        assertThatCode(() -> authService.checkEmailDuplicate(email)).doesNotThrowAnyException();
        assertThat(authService.checkEmailDuplicate(email)).isInstanceOf(SuccessResponse.class)
                .isEqualTo(success);
    }
}