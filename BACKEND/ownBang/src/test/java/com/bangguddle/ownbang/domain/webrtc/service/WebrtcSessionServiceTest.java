package com.bangguddle.ownbang.domain.webrtc.service;

import com.bangguddle.ownbang.domain.webrtc.enums.UserType;
import com.bangguddle.ownbang.domain.webrtc.service.impl.WebrtcSessionServiceImpl;
import com.bangguddle.ownbang.global.handler.AppException;
import io.openvidu.java.client.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.BAD_REQUEST;
import static com.bangguddle.ownbang.global.enums.ErrorCode.INTERNAL_SERVER_ERROR;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebrtcSessionServiceTest {

    private static final Logger log = LoggerFactory.getLogger(WebrtcServiceTest.class);

    @Mock
    private OpenVidu openVidu;

    @Mock
    private Session mockSession;

    @Mock
    private Connection mockConnection;

    @InjectMocks
    private WebrtcSessionServiceImpl webrtcSessionService;

    private Long reservationId;
    private UserType USER;
    private UserType AGENT;

    @BeforeEach
    void setUp() {
        reservationId = 10L;
        USER = UserType.USER;
        AGENT = UserType.AGENT;
    }

    @Test
    @DisplayName("세션 생성 성공")
    void 세션_생성_성공() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.getSessionId()).thenReturn("session_id");

        // when
        Optional<Session> session = webrtcSessionService.createSession(reservationId);

        // then
        assertThat(session).isPresent();
        assertThat(session.get().getSessionId()).isEqualTo("session_id");

        // verify
        verify(openVidu).createSession();
    }

    @Test
    @DisplayName("세션 생성 실패 - 기존 세션 존재")
    void 세션_생성_실패__기존_세션_존재() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.getSessionId()).thenReturn("session_id");

        // when
        Optional<Session> session = webrtcSessionService.createSession(reservationId);
        Throwable thrown = catchThrowable(() -> webrtcSessionService.createSession(reservationId));

        // then
        assertThat(session).isPresent();
        assertThat(session.get().getSessionId()).isEqualTo("session_id");
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(openVidu, times(1)).createSession();
    }

    @Test
    @DisplayName("세션 생성 실패 - 오픈 비두 장애")
    void 세션_생성_실패__오픈_비두_장애() throws Exception {
        // given
        when(openVidu.createSession()).thenThrow(OpenViduJavaClientException.class);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.createSession(reservationId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", INTERNAL_SERVER_ERROR);

        // verify
        verify(openVidu, times(1)).createSession();
    }

    @Test
    @DisplayName("세션 삭제 성공")
    void 세션_삭제_성공() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.getSessionId()).thenReturn("session_id");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // when
        Optional<Session> removedSession = webrtcSessionService.removeSession(reservationId);

        // then
        assertThat(removedSession).isPresent();
        assertThat(removedSession.get().getSessionId())
                .isEqualTo(mockSession.getSessionId());

        // verify
        verify(openVidu, times(1)).createSession();
        verify(mockSession, times(1)).close();
        assertThat(webrtcSessionService.getSession(reservationId)).isNotPresent();
    }

    @Test
    @DisplayName("세션 삭제 실패 - 유효하지 않은 세션")
    void 세션_삭제_실패__유효하지_않은_세션() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);

        // 세션 생성
        Long invalidReservationId = 123l;
        webrtcSessionService.createSession(invalidReservationId);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.removeSession(reservationId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(openVidu, times(1)).createSession();
        verify(mockSession, never()).close();
    }

    @Test
    @DisplayName("세션 삭제 실패 - 오픈 비두 장애")
    void 세션_삭제_실패__오픈_비두_장애() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        doThrow(OpenViduHttpException.class).when(mockSession).close();

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.removeSession(reservationId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", INTERNAL_SERVER_ERROR);

        // verify
        verify(openVidu, times(1)).createSession();
        verify(mockSession, times(1)).close();
    }


}
