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

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebrtcSessionServiceTest {

    private static final Logger log = LoggerFactory.getLogger(WebrtcSessionServiceImpl.class);

    @Mock
    private OpenVidu openVidu;

    @Mock
    private Session mockSession;

    @Mock
    private Connection mockConnection;

    @Mock
    private Recording mockRecording;

    @InjectMocks
    private WebrtcSessionServiceImpl webrtcSessionService;

    private Long reservationId;
    private UserType USER;
    private UserType AGENT;

    @BeforeEach
    void setUp() {
        reservationId = 10L;
        USER = UserType.ROLE_USER;
        AGENT = UserType.ROLE_AGENT;
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

    @Test
    @DisplayName("토큰 발급 성공")
    void 토큰_발급_성공() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("user-test-token");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // when
        Optional<String> token = webrtcSessionService.createToken(reservationId, USER);

        // then
        assertThat(token).isPresent();
        assertThat(token.get()).isEqualTo("user-test-token");

        // verify
        verify(mockSession, times(1)).createConnection(any());
        verify(mockConnection, times(1)).getToken();
    }

    @Test
    @DisplayName("토큰 발급 실패 - 유효하지 않은 세션")
    void 토큰_발급_실패__유효하지_않은_세션() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);

        // 세션 생성
        Long invalidReservationId = 123l;
        Optional<Session> session = webrtcSessionService.createSession(invalidReservationId);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.createToken(reservationId, USER));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(mockSession, never()).createConnection(any());
        verify(mockConnection, never()).getToken();
    }

    @Test
    @DisplayName("토큰 발급 실패 - 오픈 비두 App Server 장애 ")
    void 토큰_발급_실패__오픈_비두_App_Server_장애() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.getSessionId()).thenReturn("session_id");
        when(mockSession.createConnection(any())).thenThrow(OpenViduJavaClientException.class);

        // 세션 생성
        Optional<Session> session = webrtcSessionService.createSession(reservationId);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.createToken(reservationId, USER));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", INTERNAL_SERVER_ERROR);

        // 세션은 연결돼 있음 -> 오픈 비두 장애
        assertThat(session).isPresent();
        assertThat(session.get().getSessionId()).isEqualTo(mockSession.getSessionId());

        // verify
        verify(mockSession, times(1)).createConnection(any());
    }

    @Test
    @DisplayName("토큰 발급 실패 - 오픈 비두 장애")
    void 토큰_발급_실패__오픈_비두_장애() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.createConnection(any())).thenThrow(OpenViduHttpException.class);

        // 세션 생성
        Optional<Session> session = webrtcSessionService.createSession(reservationId);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.createToken(reservationId, USER));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", INTERNAL_SERVER_ERROR);

        // verify
        verify(mockSession, times(1)).createConnection(any());
    }

    @Test
    @DisplayName("토큰 발급 실패 - 이미 존재하는 유저 타입 - 중개인")
    void 토큰_발급_실패__이미_존재하는_유저_타입__중개인() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("agent-test-token");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        webrtcSessionService.createToken(reservationId, AGENT);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.createToken(reservationId, AGENT));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", WEBRTC_TOKEN_DUPLICATED);

        // verify
        verify(mockSession, times(1)).createConnection(any());
    }

    @Test
    @DisplayName("토큰 발급 실패 - 이미 존재하는 유저 타입 - 일반 유저")
    void 토큰_발급_실패__이미_존재하는_유저_타입__일반_유저() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("use-test-token");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        webrtcSessionService.createToken(reservationId, USER);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.createToken(reservationId, USER));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", WEBRTC_TOKEN_DUPLICATED);

        // verify
        verify(mockSession, times(1)).createConnection(any());
    }

    @Test
    @DisplayName("토큰 조회 성공")
    void 토큰_조회_성공() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("user-test-token");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        Optional<String> savedToken = webrtcSessionService.createToken(reservationId, USER);

        // when
        Optional<String> token = webrtcSessionService.getToken(reservationId, USER);

        // then
        assertThat(savedToken).isPresent();
        assertThat(token).isPresent();
        assertThat(token.get()).isEqualTo(savedToken.get());

        // verify
        verify(mockSession, times(1)).createConnection(any());
        verify(mockConnection, times(1)).getToken();
    }

    @Test
    @DisplayName("토큰_조회_실패 - 유효한 토큰 없음")
    void 토큰_조회_실패__유효한_토큰_없음() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // when
        Optional<String> token = webrtcSessionService.getToken(reservationId, USER);

        // then
        assertThat(token).isEmpty();

        // verify
        verify(mockSession, never()).createConnection(any());
        verify(mockConnection, never()).getToken();
    }

    @Test
    @DisplayName("토큰 삭제 성공")
    void 토큰_삭제_성공() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("user-test-token");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        Optional<String> savedToken = webrtcSessionService.createToken(reservationId, USER);

        // when
        Optional<String> removedToken = webrtcSessionService.removeToken(reservationId, savedToken.get(), USER);
        Optional<String> emptyToken = webrtcSessionService.getToken(reservationId, USER);

        // then
        assertThat(savedToken).isPresent();
        assertThat(removedToken).isPresent();
        assertThat(emptyToken).isEmpty();
        assertThat(savedToken.get()).isEqualTo(removedToken.get());
    }

    @Test
    @DisplayName("토큰 삭제 실패 - 토큰 없음")
    void 토큰_삭제_실패__토큰_없음() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        
        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // when
        Throwable thrown = catchThrowable(
                () -> webrtcSessionService.removeToken(reservationId, "invalid-token", USER));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);
    }

    @Test
    @DisplayName("토큰 삭제 실패 - 유저 타입과 일치한 토큰 없음")
    void 토큰_삭제_실패__유저_타입과_일치한_토큰_없음() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("user-test-token");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        webrtcSessionService.createToken(reservationId, USER);

        // when
        Throwable thrown = catchThrowable(
                () -> webrtcSessionService.removeToken(reservationId, "user-test-token", AGENT));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);
    }

    @Test
    @DisplayName("토큰 삭제 실패 - 유효한 토큰 아님")
    void 토큰_삭제_실패__유효한_토큰_아님() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("user-test-token");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        webrtcSessionService.createToken(reservationId, USER);

        // when
        Throwable thrown = catchThrowable(
                () -> webrtcSessionService.removeToken(reservationId, "invalid-token", USER));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);
    }
    
    @Test
    @DisplayName("영상 녹화 시작 성공")
    void 영상_녹화_시작_성공() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(openVidu.startRecording(eq("test-session-id"), any(RecordingProperties.class)))
                .thenReturn(mockRecording);
        when(mockSession.getSessionId()).thenReturn("test-session-id");
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("test-token");
        when(mockRecording.getSessionId()).thenReturn("test-session-id");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        webrtcSessionService.createToken(reservationId, AGENT);
        webrtcSessionService.createToken(reservationId, USER);

        // when
        Optional<Recording> recording = webrtcSessionService.startRecord(reservationId);
        
        // then
        assertThat(recording).isPresent();
        assertThat(recording.get().getSessionId()).isEqualTo(mockSession.getSessionId());

        // verify
        verify(openVidu, times(1))
                .startRecording(eq("test-session-id"), any(RecordingProperties.class));
    }

    @Test
    @DisplayName("영상 녹화 시작 실패 - 이미 녹화중인 세션")
    void 영상_녹화_시작_실패__이미_녹화중인_세션() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(openVidu.startRecording(eq("test-session-id"), any(RecordingProperties.class)))
                .thenReturn(mockRecording);
        when(mockSession.getSessionId()).thenReturn("test-session-id");
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("test-token");
        when(mockRecording.getSessionId()).thenReturn("test-session-id");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        webrtcSessionService.createToken(reservationId, AGENT);
        webrtcSessionService.createToken(reservationId, USER);

        // when
        Optional<Recording> recording = webrtcSessionService.startRecord(reservationId);
        Throwable thrown = catchThrowable(() -> webrtcSessionService.startRecord(reservationId));

        // then
        assertThat(recording).isPresent();
        assertThat(recording.get().getSessionId()).isEqualTo(mockSession.getSessionId());
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(openVidu, times(1))
                .startRecording(eq("test-session-id"), any(RecordingProperties.class));
    }

    @Test
    @DisplayName("영상 녹화 시작 실패 - 인원 부족")
    void 영상_녹화_시작_실패__인원_부족() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("test-token");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        webrtcSessionService.createToken(reservationId, AGENT);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.startRecord(reservationId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(openVidu, never())
                .startRecording(eq("test-session-id"), any(RecordingProperties.class));
    }

    @Test
    @DisplayName("영상 녹화 시작 실패 - 오픈 비두 장애")
    void 영상_녹화_시작_실패__오픈_비두_장애() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(openVidu.startRecording(eq("test-session-id"), any(RecordingProperties.class)))
                .thenThrow(OpenViduHttpException.class);
        when(mockSession.getSessionId()).thenReturn("test-session-id");
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("test-token");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        webrtcSessionService.createToken(reservationId, AGENT);
        webrtcSessionService.createToken(reservationId, USER);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.startRecord(reservationId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", WEBRTC_NO_PUBLISHER);

        // verify
        verify(openVidu, times(1))
                .startRecording(eq("test-session-id"), any(RecordingProperties.class));
    }

    @Test
    @DisplayName("영상 녹화 중지 성공")
    void 영상_녹화_중지_성공() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(openVidu.startRecording(eq("test-session-id"), any(RecordingProperties.class)))
                .thenReturn(mockRecording);
        when(openVidu.stopRecording(any())).thenReturn(mockRecording);
        when(mockSession.getSessionId()).thenReturn("test-session-id");
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("test-token");
        when(mockRecording.getSessionId()).thenReturn("test-session-id");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        webrtcSessionService.createToken(reservationId, AGENT);
        webrtcSessionService.createToken(reservationId, USER);

        // 영상 녹화
        webrtcSessionService.startRecord(reservationId);

        // when
        Optional<Recording> recording = webrtcSessionService.stopRecord(reservationId);

        // then
        assertThat(recording).isPresent();
        assertThat(recording.get().getSessionId()).isEqualTo(mockSession.getSessionId());

        // verify
        verify(openVidu, times(1))
                .startRecording(eq("test-session-id"), any(RecordingProperties.class));
        verify(openVidu, times(1))
                .stopRecording(any());
    }

    @Test
    @DisplayName("영상 녹화 중지 실패 - 세션 없음")
    void 영상_녹화_중지_실패__세션_없음() throws Exception {
        // given & when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.stopRecord(reservationId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(openVidu, never()).stopRecording(any());
    }

    @Test
    @DisplayName("영상 녹화 중지 실패 - 녹화 중인 세션 아님")
    void 영상_녹화_중지_실패__녹화_중인_세션_아님() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.stopRecord(reservationId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(openVidu, never()).stopRecording(any());
    }
    
    @Test
    @DisplayName("영상 녹화 중지 실패 - 오픈 비두 장애")
    void 영상_녹화_중지_실패__오픈_비두_장애() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(openVidu.startRecording(eq("test-session-id"), any(RecordingProperties.class)))
                .thenReturn(mockRecording);
        when(openVidu.stopRecording(any())).thenThrow(OpenViduHttpException.class);
        when(mockSession.getSessionId()).thenReturn("test-session-id");
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("test-token");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        webrtcSessionService.createToken(reservationId, AGENT);
        webrtcSessionService.createToken(reservationId, USER);

        // 영상 녹화
        webrtcSessionService.startRecord(reservationId);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.stopRecord(reservationId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", INTERNAL_SERVER_ERROR);

        // verify
        verify(openVidu, times(1)).stopRecording(any());
    }
    
    @Test
    @DisplayName("영상 녹화 삭제 성공")
    void 영상_녹화_삭제_성공() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(openVidu.startRecording(eq("test-session-id"), any(RecordingProperties.class)))
                .thenReturn(mockRecording);
        doNothing().when(openVidu).deleteRecording(any());
        when(mockSession.getSessionId()).thenReturn("test-session-id");
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("test-token");
        when(mockRecording.getId()).thenReturn("test-record-id");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        webrtcSessionService.createToken(reservationId, AGENT);
        webrtcSessionService.createToken(reservationId, USER);

        // 영상 녹화
        webrtcSessionService.startRecord(reservationId);
        
        // when
        Optional<Recording> recording = webrtcSessionService.deleteRecord(reservationId);
        
        // then
        assertThat(recording).isPresent();
        assertThat(recording.get().getId()).isEqualTo(mockRecording.getId());

        // verify
        verify(openVidu, times(1)).deleteRecording(any());
    }

    @Test
    @DisplayName("영상 녹화 삭제 실패 - 세션 없음")
    void 영상_녹화_삭제_실패__세션_없음() throws Exception {
        // given & when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.deleteRecord(reservationId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(openVidu, never()).deleteRecording(any());
    }

    @Test
    @DisplayName("영상 녹화 삭제 실패 - 녹화 중인 세션 아님")
    void 영상_녹화_삭제_실패__녹화_중인_세션_아님() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.deleteRecord(reservationId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(openVidu, never()).deleteRecording(any());
    }

    @Test
    @DisplayName("영상 녹화 삭제 실패 - 오픈 비두 장애")
    void 영상_녹화_삭제_실패__오픈_비두_장애() throws Exception {
        // given
        when(openVidu.createSession()).thenReturn(mockSession);
        when(openVidu.startRecording(eq("test-session-id"), any(RecordingProperties.class)))
                .thenReturn(mockRecording);
        doThrow(OpenViduHttpException.class).when(openVidu).deleteRecording(any());
        when(mockSession.getSessionId()).thenReturn("test-session-id");
        when(mockSession.createConnection(any())).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("test-token");
        when(mockRecording.getId()).thenReturn("test-record-id");

        // 세션 생성
        webrtcSessionService.createSession(reservationId);

        // 토큰 생성
        webrtcSessionService.createToken(reservationId, AGENT);
        webrtcSessionService.createToken(reservationId, USER);

        // 영상 녹화
        webrtcSessionService.startRecord(reservationId);

        // when
        Throwable thrown = catchThrowable(() -> webrtcSessionService.deleteRecord(reservationId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", INTERNAL_SERVER_ERROR);

        // verify
        verify(openVidu, times(1)).deleteRecording(any());
    }
}
