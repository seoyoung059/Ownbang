package com.bangguddle.ownbang.domain.auth.service;

import com.bangguddle.ownbang.domain.auth.dto.DuplicateResponse;
import com.bangguddle.ownbang.domain.auth.dto.LoginRequest;
import com.bangguddle.ownbang.domain.auth.dto.UserSignUpRequest;
import com.bangguddle.ownbang.domain.auth.service.impl.AuthServiceImpl;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.config.security.JwtProvider;
import com.bangguddle.ownbang.global.dto.Tokens;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.repository.RedisRepository;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.EMAIL_DUPLICATED;
import static com.bangguddle.ownbang.global.enums.ErrorCode.PHONE_NUMBER_DUPLICATED;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
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
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private RedisRepository redisRepository;
    @Mock
    private User mockUser;

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
                new SuccessResponse<>(SIGNUP_SUCCESS, NoneResponse.NONE);

        // when
        doReturn(Optional.empty()).when(userRepository).findByEmail(request.email());
        doReturn(Optional.empty()).when(userRepository).findByPhoneNumber(request.phoneNumber());

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
        doThrow(new AppException(PHONE_NUMBER_DUPLICATED))
                .when(userRepository).findByPhoneNumber(request.phoneNumber());

        // then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(PHONE_NUMBER_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("회원 가입 실패 - 이메일 중복")
    public void 회원_가입_실패_이메일_중복() {
        // given
        UserSignUpRequest request = UserSignUpRequest.builder()
                .email("email")
                .build();

        // when
        doThrow(new AppException(EMAIL_DUPLICATED))
                .when(userRepository).findByEmail(request.email());

        // then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(EMAIL_DUPLICATED.getMessage());
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

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // then
        assertThatCode(() -> authService.checkEmailDuplicate(email)).doesNotThrowAnyException();
        assertThat(authService.checkEmailDuplicate(email)).isInstanceOf(SuccessResponse.class)
                .isEqualTo(success);
    }

    @Test
    @DisplayName("전화번호 중복 확인 성공 - 중복됨")
    public void 전화번호_중복_확인_성공_중복됨() {
        // given
        String phoneNumber = "isDuplicate";
        DuplicateResponse response = new DuplicateResponse(true);
        SuccessResponse<DuplicateResponse> success =
                new SuccessResponse<>(CHECK_PHONE_NUMBER_DUPLICATE_SUCCESS, response);
        User user = User.builder().phoneNumber(phoneNumber).build();

        // when
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));

        // then
        assertThatCode(() -> authService.checkPhoneNumberDuplicate(phoneNumber)).doesNotThrowAnyException();
        assertThat(authService.checkPhoneNumberDuplicate(phoneNumber)).isInstanceOf(SuccessResponse.class)
                .isEqualTo(success);
    }

    @Test
    @DisplayName("전화번호 중복 확인 성공 - 중복되지 않음")
    public void 전화번호_중복_확인_성공_중복되지않음() {
        // given
        String phoneNumber = "isNotDuplicate";
        DuplicateResponse response = new DuplicateResponse(false);
        SuccessResponse<DuplicateResponse> success =
                new SuccessResponse<>(CHECK_PHONE_NUMBER_DUPLICATE_SUCCESS, response);

        // when
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        // then
        assertThatCode(() -> authService.checkPhoneNumberDuplicate(phoneNumber)).doesNotThrowAnyException();
        assertThat(authService.checkPhoneNumberDuplicate(phoneNumber)).isInstanceOf(SuccessResponse.class)
                .isEqualTo(success);
    }

    @Test
    @DisplayName("로그인 성공")
    public void 로그인_성공() {
        // given
        String email = "email", password = "password";
        String accessToken = "access", refreshToken = "refresh";
        Long userId = 1L;
        LoginRequest request = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        Tokens tokens = Tokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        SuccessResponse<Tokens> expected =
                new SuccessResponse<>(LOGIN_SUCCESS, tokens);


        // when
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(mockUser));
        when(mockUser.getId()).thenReturn(userId);
        when(jwtProvider.generateTokens(userId)).thenReturn(tokens);
        doNothing().when(redisRepository).save(tokens);
        doNothing().when(redisRepository).saveValidTokens(tokens, userId);


        // then
        assertThatCode(() -> authService.login(request)).doesNotThrowAnyException();
        assertThat(authService.login(request)).isInstanceOf(SuccessResponse.class)
                .isEqualTo(expected);

    }

    @Test
    @DisplayName("로그인 실패 - 아이디또는 비밀번호가 유효하지 않음")
    public void 로그인_실패_아이디또는_비밀번호가_유효하지_않음() {
        // given
        String email = "invalid", password = "invalid";

        LoginRequest request = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        // when
        doThrow(BadCredentialsException.class)
                .when(authenticationManager).authenticate(any());

        // then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

}