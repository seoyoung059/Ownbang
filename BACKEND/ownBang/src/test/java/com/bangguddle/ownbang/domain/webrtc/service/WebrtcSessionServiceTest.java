package com.bangguddle.ownbang.domain.webrtc.service;

import com.bangguddle.ownbang.domain.webrtc.entity.WebrtcSession;
import com.bangguddle.ownbang.domain.webrtc.repository.WebrtcSessionRepository;
import com.bangguddle.ownbang.domain.webrtc.service.impl.WebrtcSessionServiceImpl;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebrtcSessionServiceTest {

    private static final Logger log = LoggerFactory.getLogger(WebrtcServiceTest.class);

    @Mock
    private OpenVidu openVidu;

    @Mock
    private WebrtcSessionRepository webrtcSessionRepository;

    @InjectMocks
    private WebrtcSessionServiceImpl webrtcSessionService;

    @Test
    @DisplayName("새로운 세션 생성 성공")
    void 새로운_세션_생성_성공() throws Exception {
        // given
        Long reservationId = 10l;
        Session mockSession = mock(Session.class);

        when(webrtcSessionRepository.findByReservationId(reservationId)).thenReturn(Optional.empty());
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.getSessionId()).thenReturn("newSessionId");
        when(webrtcSessionRepository.save(any(WebrtcSession.class))).thenAnswer(i -> i.getArgument(0));

        // when
        Optional<WebrtcSession> session = webrtcSessionService.getSession(reservationId);

        // then
        assertThat(session).isPresent();
        assertThat(session.get().getSession()).isEqualTo("newSessionId");

        // verify
        verify(webrtcSessionRepository).findByReservationId(reservationId);
        verify(openVidu).createSession();
        verify(webrtcSessionRepository).save(any(WebrtcSession.class));
    }
    
    @Test
    @DisplayName("기존 세션 반환 성공")
    void 기존_세션_반환_성공() throws Exception {
        // given
        Long reservationId = 10L;
        WebrtcSession existingSession = WebrtcSession.builder().session("existingSessionId").build();

        // mock
        when(webrtcSessionRepository.findByReservationId(reservationId)).thenReturn(Optional.of(existingSession));

        // when
        Optional<WebrtcSession> session = webrtcSessionService.getSession(reservationId);

        // then
        assertThat(session).isPresent();
        assertThat(session.get().getSession()).isEqualTo("existingSessionId");

        // verify
        verify(webrtcSessionRepository).findByReservationId(reservationId);
        verify(openVidu, never()).createSession();
        
    }
}
