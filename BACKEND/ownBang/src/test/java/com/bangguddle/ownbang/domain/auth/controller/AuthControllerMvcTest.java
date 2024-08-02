package com.bangguddle.ownbang.domain.auth.controller;

import com.bangguddle.ownbang.domain.auth.dto.DuplicateResponse;
import com.bangguddle.ownbang.domain.auth.dto.LoginRequest;
import com.bangguddle.ownbang.domain.auth.dto.UserSignUpRequest;
import com.bangguddle.ownbang.domain.auth.service.AuthService;
import com.bangguddle.ownbang.global.dto.Tokens;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.ErrorCode.BAD_CREDENTIAL;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
                {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OncePerRequestFilter.class})})
public class AuthControllerMvcTest {
    @MockBean
    private AuthService authService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final String SUCCESS = "SUCCESS", ERROR = "ERROR";

    @AllArgsConstructor
    @Getter
    private enum URL{
        SIGN_UP("/auths/sign-up"),
        EMAIL("/auths/duplicates/email"),
        PHONE("/auths/duplicates/phone"),
        LOGIN("/auths/login");
        final String url;
    }

    private UserSignUpRequest makeSignUpRequest(String name) {
        return UserSignUpRequest.builder()
                .name(name)
                .build();
    }

    @Test
    @DisplayName("회원 가입 성공")
    public void 회원_가입_성공() throws Exception {
        // given
        String url = URL.SIGN_UP.url;
        UserSignUpRequest newUserRequest = makeSignUpRequest("new");
        SuccessResponse<NoneResponse> success =
                new SuccessResponse<>(SIGNUP_SUCCESS, NoneResponse.NONE);
        when(authService.signUp(newUserRequest)).thenReturn(success);

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserRequest))
                        .with(csrf())
        );

        actions.andExpect(status().is(SIGNUP_SUCCESS.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(SIGNUP_SUCCESS.name()))
                .andExpect(jsonPath("$.data").value(NoneResponse.NONE.name()))
                .andExpect(jsonPath("$.resultCode").value(SUCCESS))
                .andExpect(jsonPath("$.message").value(SIGNUP_SUCCESS.getMessage()))
                .andDo(print());

    }

    @Test
    @DisplayName("회원 가입 실패 - 전화번호 중복")
    public void 회원_가입_실패_전화번호_중복() throws Exception {
        // given
        String url = URL.SIGN_UP.url;
        UserSignUpRequest pExistUserRequest = makeSignUpRequest("phone");
        doThrow(new AppException(PHONE_NUMBER_DUPLICATED))
                .when(authService).signUp(pExistUserRequest);

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pExistUserRequest))
                        .with(csrf())
        );

        // then
        actions.andExpect(status().is(PHONE_NUMBER_DUPLICATED.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(PHONE_NUMBER_DUPLICATED.name()))
                .andExpect(jsonPath("$.data").value(NoneResponse.NONE.name()))
                .andExpect(jsonPath("$.resultCode").value(ERROR))
                .andExpect(jsonPath("$.message").value(PHONE_NUMBER_DUPLICATED.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 실패 - 이메일 중복")
    public void 회원_가입_실패_이메일_중복() throws Exception {
        // given
        String url = URL.SIGN_UP.url;
        UserSignUpRequest eExistUserRequest = makeSignUpRequest("email");
        doThrow(new AppException(EMAIL_DUPLICATED))
                .when(authService).signUp(eExistUserRequest);

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eExistUserRequest))
                        .with(csrf())
        );

        // then
        actions.andExpect(status().is(EMAIL_DUPLICATED.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(EMAIL_DUPLICATED.name()))
                .andExpect(jsonPath("$.data").value(NoneResponse.NONE.name()))
                .andExpect(jsonPath("$.resultCode").value(ERROR))
                .andExpect(jsonPath("$.message").value(EMAIL_DUPLICATED.getMessage()))
                .andDo(print());

    }

    @Test
    @DisplayName("이메일 중복 확인 성공 - 중복되지 않음")
    public void 이메일_중복_확인_성공_중복되지_않음() throws Exception {
        // given
        String url = URL.EMAIL.url;
        String email = "new";
        SuccessResponse<DuplicateResponse> success =
                new SuccessResponse<>(CHECK_EMAIL_DUPLICATE_SUCCESS,
                        new DuplicateResponse(false));
        when(authService.checkEmailDuplicate(email)).thenReturn(success);

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("email", email)
                        .with(csrf())
        );

        // then
        actions.andExpect(status().is(CHECK_EMAIL_DUPLICATE_SUCCESS.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(CHECK_EMAIL_DUPLICATE_SUCCESS.name()))
                .andExpect(jsonPath("$.data.isDuplicate").value(false))
                .andExpect(jsonPath("$.resultCode").value(SUCCESS))
                .andExpect(jsonPath("$.message").value(CHECK_EMAIL_DUPLICATE_SUCCESS.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 중복 확인 성공 - 중복됨")
    public void 이메일_중복_확인_성공_중복됨() throws Exception {
        // given
        String url = URL.EMAIL.url;
        String email = "exist";
        SuccessResponse<DuplicateResponse> success =
                new SuccessResponse<>(CHECK_EMAIL_DUPLICATE_SUCCESS,
                        new DuplicateResponse(true));
        when(authService.checkEmailDuplicate(email)).thenReturn(success);

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("email", email)
                        .with(csrf())
        );

        // then
        actions.andExpect(status().is(CHECK_EMAIL_DUPLICATE_SUCCESS.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(CHECK_EMAIL_DUPLICATE_SUCCESS.name()))
                .andExpect(jsonPath("$.data.isDuplicate").value(true))
                .andExpect(jsonPath("$.resultCode").value(SUCCESS))
                .andExpect(jsonPath("$.message").value(CHECK_EMAIL_DUPLICATE_SUCCESS.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("전화번호 중복 확인 성공 - 중복되지 않음")
    public void 젼화번호_중복_확인_성공_중복되지_않음() throws Exception {
        // given
        String url = URL.PHONE.url;
        String phone = "new";
        SuccessResponse<DuplicateResponse> success =
                new SuccessResponse<>(CHECK_PHONE_NUMBER_DUPLICATE_SUCCESS,
                        new DuplicateResponse(false));
        when(authService.checkPhoneNumberDuplicate(phone)).thenReturn(success);

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("phoneNumber", phone)
                        .with(csrf())
        );

        // then
        actions.andExpect(status().is(CHECK_PHONE_NUMBER_DUPLICATE_SUCCESS.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(CHECK_PHONE_NUMBER_DUPLICATE_SUCCESS.name()))
                .andExpect(jsonPath("$.data.isDuplicate").value(false))
                .andExpect(jsonPath("$.resultCode").value(SUCCESS))
                .andExpect(jsonPath("$.message").value(CHECK_PHONE_NUMBER_DUPLICATE_SUCCESS.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("전화번호 중복 확인 성공 - 중복됨")
    public void 전화번호_중복_확인_성공_중복됨() throws Exception {
        // given
        String url = URL.PHONE.url;
        String phone = "exist";
        SuccessResponse<DuplicateResponse> success =
                new SuccessResponse<>(CHECK_PHONE_NUMBER_DUPLICATE_SUCCESS,
                        new DuplicateResponse(true));
        when(authService.checkPhoneNumberDuplicate(phone)).thenReturn(success);

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("phoneNumber", phone)
                        .with(csrf())
        );

        // then
        actions.andExpect(status().is(CHECK_PHONE_NUMBER_DUPLICATE_SUCCESS.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(CHECK_PHONE_NUMBER_DUPLICATE_SUCCESS.name()))
                .andExpect(jsonPath("$.data.isDuplicate").value(true))
                .andExpect(jsonPath("$.resultCode").value(SUCCESS))
                .andExpect(jsonPath("$.message").value(CHECK_PHONE_NUMBER_DUPLICATE_SUCCESS.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공")
    public void 로그인_성공() throws Exception {
        // given
        String url = URL.LOGIN.url;
        String email = "email", password = "password";
        String accessToken = "access", refreshToken = "refresh";

        LoginRequest request = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        Tokens tokens = Tokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        SuccessResponse<Tokens> success =
                new SuccessResponse<>(LOGIN_SUCCESS, tokens);
        when(authService.login(request)).thenReturn(success);

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
        );

        actions.andExpect(status().is(LOGIN_SUCCESS.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(LOGIN_SUCCESS.name()))
                .andExpect(jsonPath("$.data.accessToken").value(tokens.accessToken()))
                .andExpect(jsonPath("$.data.refreshToken").value(tokens.refreshToken()))
                .andExpect(jsonPath("$.resultCode").value(SUCCESS))
                .andExpect(jsonPath("$.message").value(LOGIN_SUCCESS.getMessage()))
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 실패 - 아이디또는 비밀번호가 유효하지 않음")
    public void 로그인_실패_아이디_또는_비빌번호가_유효하지_않음() throws Exception {
        // given
        String url = URL.LOGIN.url;
        String email = "invalid", password = "invalid";

        LoginRequest inValidRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        doThrow(BadCredentialsException.class)
                .when(authService).login(inValidRequest);

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inValidRequest))
                        .with(csrf())
        );

        // then
        actions.andExpect(status().is(BAD_CREDENTIAL.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(BAD_CREDENTIAL.name()))
                .andExpect(jsonPath("$.data").value(NoneResponse.NONE.name()))
                .andExpect(jsonPath("$.resultCode").value(ERROR))
                .andExpect(jsonPath("$.message").value(BAD_CREDENTIAL.getMessage()))
                .andDo(print());
    }

}
