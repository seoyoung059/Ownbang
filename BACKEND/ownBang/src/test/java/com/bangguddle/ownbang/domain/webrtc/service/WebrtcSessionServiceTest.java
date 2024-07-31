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




}
