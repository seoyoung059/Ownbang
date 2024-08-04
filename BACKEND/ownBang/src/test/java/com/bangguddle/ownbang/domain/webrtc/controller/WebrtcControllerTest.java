package com.bangguddle.ownbang.domain.webrtc.controller;

import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcCreateTokenRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcRemoveTokenRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcTokenResponse;
import com.bangguddle.ownbang.domain.webrtc.service.impl.WebrtcAgentService;
import com.bangguddle.ownbang.domain.webrtc.service.impl.WebrtcUserService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebrtcControllerTest {

    @Mock
    private WebrtcUserService webrtcUserService;
    @Mock
    private WebrtcAgentService webrtcAgentService;
    @Mock
    private Authentication authentication;
    @Mock
    private List grantedAuthority;

    @InjectMocks
    private WebrtcController webrtcController;

    private Long userId;
    private String token;

    private WebrtcCreateTokenRequest makeCreateRequest(Long reservationId){
        return WebrtcCreateTokenRequest.builder()
                .reservationId(reservationId)
                .build();
    }

    private WebrtcRemoveTokenRequest makeRemoveRequest(Long reservationId, String token){
        return WebrtcRemoveTokenRequest.builder()
                .reservationId(reservationId)
                .token(token)
                .build();
    }

    @BeforeEach
    void setup(){
        userId = 10L;
        token = "test-token";

        when(authentication.getAuthorities()).thenReturn(grantedAuthority);
    }

    @Test
    @DisplayName("중개인 토큰 생성 성공")
    void 중개인_토큰_생성_성공() throws Exception {
        // given
        WebrtcCreateTokenRequest createRequest = makeCreateRequest(10L);
        WebrtcTokenResponse response = WebrtcTokenResponse.builder().token(token).build();
        SuccessResponse success =
                new SuccessResponse<>(GET_TOKEN_SUCCESS, response);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(true);
        when(webrtcAgentService.getToken(createRequest, userId))
                .thenReturn(success);

        // then
        assertThatCode(() ->webrtcController.getToken(createRequest, userId, authentication))
                .doesNotThrowAnyException();
        assertThat(webrtcController.getToken(createRequest, userId, authentication).getStatusCode())
                .isEqualTo(GET_TOKEN_SUCCESS.getHttpStatus());
        assertThat(webrtcController.getToken(createRequest, userId, authentication))
                .isInstanceOf(ResponseEntity.class)
                .isEqualTo(Response.success(success));

        // verify
        verify(webrtcAgentService, atLeast(1)).getToken(any(), any());
        verify(webrtcUserService, never()).getToken(any(), any());
    }

    @Test
    @DisplayName("중개인 토큰 생성 실패 - 유효하지 않은 예약")
    void 중개인_토큰_생성_실패__유효하지_않은_예약() throws Exception {
        // given
        WebrtcCreateTokenRequest eInvalidRequest = makeCreateRequest(100L);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(true);
        when(webrtcAgentService.getToken(eInvalidRequest, userId))
                .thenThrow(new AppException(RESERVATION_NOT_FOUND));

        // then
        assertThatThrownBy(() -> webrtcController.getToken(eInvalidRequest, userId, authentication))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_NOT_FOUND);

        // verify
        verify(webrtcAgentService, atLeast(1)).getToken(any(), any());
        verify(webrtcUserService, never()).getToken(any(), any());
    }

    @Test
    @DisplayName("중개인 토큰 생성 실패 - 확정된 예약이 아닌 경우")
    void 중개인_토큰_생성_실패__확정된_예약이_아닌_경우() throws Exception {
        // given
        WebrtcCreateTokenRequest eInvalidRequest = makeCreateRequest(100L);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(true);
        when(webrtcAgentService.getToken(eInvalidRequest, userId))
                .thenThrow(new AppException(RESERVATION_STATUS_NOT_CONFIRMED));

        // then
        assertThatThrownBy(() -> webrtcController.getToken(eInvalidRequest, userId, authentication))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_STATUS_NOT_CONFIRMED);

        // verify
        verify(webrtcAgentService, atLeast(1)).getToken(any(), any());
        verify(webrtcUserService, never()).getToken(any(), any());
    }

    @Test
    @DisplayName("중개인 토큰 생성 실패 - 이미 존재하는 세션")
    void 중개인_토큰_생성_실패__이미_존재하는_세션() throws Exception {
        // given
        WebrtcCreateTokenRequest eInvalidRequest = makeCreateRequest(100L);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(true);
        when(webrtcAgentService.getToken(eInvalidRequest, userId))
                .thenThrow(new AppException(WEBRTC_SESSION_DUPLICATED));

        // then
        assertThatThrownBy(() -> webrtcController.getToken(eInvalidRequest, userId, authentication))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", WEBRTC_SESSION_DUPLICATED);

        // verify
        verify(webrtcAgentService, atLeast(1)).getToken(any(), any());
        verify(webrtcUserService, never()).getToken(any(), any());
    }

    @Test
    @DisplayName("중개인 토큰 생성 실패 - 오픈 비두 장애")
    void 중개인_토큰_생성_실패__오픈_비두_장애() throws Exception {
        // given
        WebrtcCreateTokenRequest createRequest = makeCreateRequest(100L);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(true);
        when(webrtcAgentService.getToken(createRequest, userId))
                .thenThrow(new AppException(INTERNAL_SERVER_ERROR));

        // then
        assertThatThrownBy(() -> webrtcController.getToken(createRequest, userId, authentication))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", INTERNAL_SERVER_ERROR);

        // verify
        verify(webrtcAgentService, atLeast(1)).getToken(any(), any());
        verify(webrtcUserService, never()).getToken(any(), any());
    }

    @Test
    @DisplayName("임차인 토큰 생성 성공")
    void 임차인_토큰_생성_성공() throws Exception {
        // given
        WebrtcCreateTokenRequest createRequest = makeCreateRequest(10L);
        WebrtcTokenResponse response = WebrtcTokenResponse.builder().token(token).build();
        SuccessResponse success =
                new SuccessResponse<>(GET_TOKEN_SUCCESS, response);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(false);
        when(webrtcUserService.getToken(createRequest, userId))
                .thenReturn(success);

        // then
        assertThatCode(() ->webrtcController.getToken(createRequest, userId, authentication))
                .doesNotThrowAnyException();
        assertThat(webrtcController.getToken(createRequest, userId, authentication).getStatusCode())
                .isEqualTo(GET_TOKEN_SUCCESS.getHttpStatus());
        assertThat(webrtcController.getToken(createRequest, userId, authentication))
                .isInstanceOf(ResponseEntity.class)
                .isEqualTo(Response.success(success));

        // verify
        verify(webrtcAgentService, never()).getToken(any(), any());
        verify(webrtcUserService, atLeast(1)).getToken(any(), any());
    }

    @Test
    @DisplayName("임차인 토큰 생성 실패 - 유효하지 않은 예약")
    void 임차인_토큰_생성_실패__유효하지_않은_예약() throws Exception {
        // given
        WebrtcCreateTokenRequest eInvalidRequest = makeCreateRequest(100L);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(false);
        when(webrtcUserService.getToken(eInvalidRequest, userId))
                .thenThrow(new AppException(RESERVATION_NOT_FOUND));

        // then
        assertThatThrownBy(() -> webrtcController.getToken(eInvalidRequest, userId, authentication))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_NOT_FOUND);

        // verify
        verify(webrtcAgentService, never()).getToken(any(), any());
        verify(webrtcUserService, atLeast(1)).getToken(any(), any());
    }

    @Test
    @DisplayName("임차인 토큰 생성 실패 - 확정된 예약이 아닌 경우")
    void 임차인_토큰_생성_실패__확정된_예약이_아닌_경우() throws Exception {
        // given
        WebrtcCreateTokenRequest eInvalidRequest = makeCreateRequest(100L);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(false);
        when(webrtcUserService.getToken(eInvalidRequest, userId))
                .thenThrow(new AppException(RESERVATION_STATUS_NOT_CONFIRMED));

        // then
        assertThatThrownBy(() -> webrtcController.getToken(eInvalidRequest, userId, authentication))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_STATUS_NOT_CONFIRMED);

        // verify
        verify(webrtcAgentService, never()).getToken(any(), any());
        verify(webrtcUserService, atLeast(1)).getToken(any(), any());
    }

    @Test
    @DisplayName("임차인 토큰 생성 실패 - 없는 세션")
    void 임차인_토큰_생성_실패__없는_세션() throws Exception {
        // given
        WebrtcCreateTokenRequest eInvalidRequest = makeCreateRequest(100L);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(false);
        when(webrtcUserService.getToken(eInvalidRequest, userId))
                .thenThrow(new AppException(BAD_REQUEST));

        // then
        assertThatThrownBy(() -> webrtcController.getToken(eInvalidRequest, userId, authentication))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(webrtcAgentService, never()).getToken(any(), any());
        verify(webrtcUserService, atLeast(1)).getToken(any(), any());
    }

    @Test
    @DisplayName("임차인 토큰 생성 실패 - 오픈 비두 장애")
    void 임차인_토큰_생성_실패__오픈_비두_장애() throws Exception {
        // given
        WebrtcCreateTokenRequest createRequest = makeCreateRequest(100L);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(false);
        when(webrtcUserService.getToken(createRequest, userId))
                .thenThrow(new AppException(INTERNAL_SERVER_ERROR));

        // then
        assertThatThrownBy(() -> webrtcController.getToken(createRequest, userId, authentication))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", INTERNAL_SERVER_ERROR);

        // verify
        verify(webrtcAgentService, never()).getToken(any(), any());
        verify(webrtcUserService, atLeast(1)).getToken(any(), any());
    }

    @Test
    @DisplayName("중개인 토큰 삭제 성공")
    void 중개인_토큰_삭제_성공() throws Exception {
        // given
        WebrtcRemoveTokenRequest removeRequest = makeRemoveRequest(10L, token);
        SuccessResponse success =
                new SuccessResponse<>(REMOVE_TOKEN_SUCCESS, NoneResponse.NONE);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(true);
        when(webrtcAgentService.removeToken(removeRequest, userId))
                .thenReturn(success);

        // then
        assertThatCode(() ->webrtcController.removeToken(removeRequest, userId, authentication))
                .doesNotThrowAnyException();
        assertThat(webrtcController.removeToken(removeRequest, userId, authentication).getStatusCode())
                .isEqualTo(REMOVE_TOKEN_SUCCESS.getHttpStatus());
        assertThat(webrtcController.removeToken(removeRequest, userId, authentication))
                .isInstanceOf(ResponseEntity.class)
                .isEqualTo(Response.success(success));

        // verify
        verify(webrtcAgentService, atLeast(1)).removeToken(any(), any());
        verify(webrtcUserService, never()).removeToken(any(), any());
    }

    @Test
    @DisplayName("중개인 토큰 삭제 실패 - 유효하지 않은 예약")
    void 중개인_토큰_삭제_실패__유효하지_않은_예약() throws Exception {
        // given
        WebrtcRemoveTokenRequest removeRequest = makeRemoveRequest(10L, token);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(true);
        when(webrtcAgentService.removeToken(removeRequest, userId))
                .thenThrow(new AppException(RESERVATION_NOT_FOUND));

        // then
        assertThatThrownBy(() -> webrtcController.removeToken(removeRequest, userId, authentication))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_NOT_FOUND);

        // verify
        verify(webrtcAgentService, atLeast(1)).removeToken(any(), any());
        verify(webrtcUserService, never()).removeToken(any(), any());
    }

    @Test
    @DisplayName("중개인 토큰 삭제 실패 - 확정된 예약이 아닌 경우")
    void 중개인_토큰_삭제_실패__확정된_예약이_아닌_경우() throws Exception {
        // given
        WebrtcRemoveTokenRequest removeRequest = makeRemoveRequest(10L, token);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(true);
        when(webrtcAgentService.removeToken(removeRequest, userId))
                .thenThrow(new AppException(RESERVATION_STATUS_NOT_CONFIRMED));

        // then
        assertThatThrownBy(() -> webrtcController.removeToken(removeRequest, userId, authentication))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_STATUS_NOT_CONFIRMED);

        // verify
        verify(webrtcAgentService, atLeast(1)).removeToken(any(), any());
        verify(webrtcUserService, never()).removeToken(any(), any());
    }

    @Test
    @DisplayName("중개인 토큰 삭제 실패 - 이미 삭제된 세션")
    void 중개인_토큰_삭제_실패__이미_삭제된_세션() throws Exception {
        // given
        WebrtcRemoveTokenRequest removeRequest = makeRemoveRequest(10L, token);

        // when
        when(grantedAuthority.contains(new SimpleGrantedAuthority("ROLE_AGENT")))
                .thenReturn(true);
        when(webrtcAgentService.removeToken(removeRequest, userId))
                .thenThrow(new AppException(BAD_REQUEST));

        // then
        assertThatThrownBy(() -> webrtcController.removeToken(removeRequest, userId, authentication))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(webrtcAgentService, atLeast(1)).removeToken(any(), any());
        verify(webrtcUserService, never()).removeToken(any(), any());
    }
}
