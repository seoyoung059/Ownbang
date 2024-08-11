package com.bangguddle.ownbang.domain.webrtc.service;


import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.domain.video.service.impl.VideoServiceImpl;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcCreateTokenRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcRemoveTokenRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcTokenResponse;
import com.bangguddle.ownbang.domain.webrtc.enums.UserType;
import com.bangguddle.ownbang.domain.webrtc.service.impl.WebrtcSessionServiceImpl;
import com.bangguddle.ownbang.domain.webrtc.service.impl.WebrtcUserService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import io.openvidu.java.client.Recording;
import io.openvidu.java.client.Session;
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
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebrtcUserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(WebrtcUserServiceTest.class);

    @Mock
    private VideoServiceImpl videoService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private WebrtcSessionServiceImpl webrtcSessionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private Session mockSession;

    @Mock
    private Recording recording;

    @Mock
    private Reservation reservation;

    @InjectMocks
    private WebrtcUserService webrtcService;

    private Long reservationId;
    private Long userId;
    private UserType USER;
    private UserType AGENT;

    @BeforeEach
    void setUp() {
        reservationId = 10L;
        userId = 12311L;
        USER = UserType.ROLE_USER;
        AGENT = UserType.ROLE_AGENT;

        when(userRepository.getById(anyLong())).thenReturn(user);
    }

    @Test
    @DisplayName("토큰 생성 성공")
    void 토큰_생성_성공() throws Exception {
        // given
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        when(webrtcSessionService.getSession(reservationId)).thenReturn(Optional.of(mockSession));
        when(webrtcSessionService.createToken(reservationId, UserType.ROLE_USER))
                .thenReturn(Optional.of("test-token"));
        when(webrtcSessionService.startRecord(reservationId)).thenReturn(Optional.of(recording));

        WebrtcCreateTokenRequest request = WebrtcCreateTokenRequest.builder().reservationId(reservationId).build();

        // when
        SuccessResponse<WebrtcTokenResponse> response = webrtcService.getToken(request, userId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.data().token()).isEqualTo("test-token");
        assertThat(response.successCode()).isEqualTo(GET_TOKEN_SUCCESS);

        // verify
        verify(webrtcSessionService, times(1)).createToken(any(), any());
    }

    @Test
    @DisplayName("토큰 생성 실패 - 권한 없는 예약")
    void 토큰_생성_실패__권한_없는_예약() throws Exception {
        // given
        Long invalidUserId = 100L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(invalidUserId);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);

        WebrtcCreateTokenRequest request = WebrtcCreateTokenRequest.builder().reservationId(reservationId).build();

        // when
        Throwable thrown = catchThrowable(() -> webrtcService.getToken(request, userId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ACCESS_DENIED);

        // verify
        verify(webrtcSessionService, never()).createToken(any(), any());
    }

    @Test
    @DisplayName("토큰 발급 실패 - 없는 세션")
    void 토큰_발급_실패__없는_세션() throws Exception {
        // given
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        when(webrtcSessionService.getSession(reservationId)).thenReturn(Optional.empty());

        WebrtcCreateTokenRequest request = WebrtcCreateTokenRequest.builder().reservationId(reservationId).build();

        // when
        Throwable thrown = catchThrowable(() -> webrtcService.getToken(request, userId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", WEBRTC_SESSION_UNOPENED);

        // verify
        verify(webrtcSessionService, never()).createToken(any(), any());
    }

    @Test
    @DisplayName("토큰 발급 실패 - 없는 예약 번호")
    void 토큰_발급_실패__없는_예약_번호() throws Exception {
        // given
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        WebrtcCreateTokenRequest request = WebrtcCreateTokenRequest.builder().reservationId(reservationId).build();

        // when
        Throwable thrown = catchThrowable(() -> webrtcService.getToken(request, userId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_NOT_FOUND);

        // verify
        verify(webrtcSessionService, never()).createToken(any(), any());
    }
    
    @Test
    @DisplayName("토큰 발급 실패 - 확정된 예약이 아닌 경우")
    void 토큰_발급_실패__확정된_예약이_아닌_경우() throws Exception {
        // given
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getStatus()).thenReturn(ReservationStatus.APPLYED);

        WebrtcCreateTokenRequest request = WebrtcCreateTokenRequest.builder().reservationId(reservationId).build();

        // when
        Throwable thrown = catchThrowable(() -> webrtcService.getToken(request, userId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_STATUS_NOT_CONFIRMED);

        // verify
        verify(webrtcSessionService, never()).createToken(any(), any());
    }

    @Test
    @DisplayName("토큰 발급 실패 - 취소된 예약인 경우")
    void 토큰_발급_실패__취소된_예약인_경우() throws Exception {
        // given
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getStatus()).thenReturn(ReservationStatus.CANCELLED);

        WebrtcCreateTokenRequest request = WebrtcCreateTokenRequest.builder().reservationId(reservationId).build();

        // when
        Throwable thrown = catchThrowable(() -> webrtcService.getToken(request, userId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_STATUS_NOT_CONFIRMED);

        // verify
        verify(webrtcSessionService, never()).createToken(any(), any());
    }

    @Test
    @DisplayName("토큰 발급 실패 - 이미 존재하는 토큰")
    void 토큰_발급_실패__이미_존재하는_토큰() throws Exception {
        // given
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        when(webrtcSessionService.getSession(reservationId)).thenReturn(Optional.of(mockSession));
        when(webrtcSessionService.createToken(reservationId, UserType.ROLE_USER))
                .thenThrow(new AppException(WEBRTC_TOKEN_DUPLICATED));

        WebrtcCreateTokenRequest request = WebrtcCreateTokenRequest.builder().reservationId(reservationId).build();

        // when
        Throwable thrown = catchThrowable(() -> webrtcService.getToken(request, userId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", WEBRTC_TOKEN_DUPLICATED);
    }

    @Test
    @DisplayName("토큰 발급 실패 - 영상 녹화 실패")
    void 토큰_발급_실패__영상_녹화_실패() throws Exception {
        // given
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        when(webrtcSessionService.getSession(reservationId)).thenReturn(Optional.of(mockSession));
        when(webrtcSessionService.createToken(reservationId, UserType.ROLE_USER))
                .thenReturn(Optional.of("test-token"));
        when(webrtcSessionService.startRecord(reservationId)).thenThrow(new AppException(WEBRTC_NO_PUBLISHER));

        WebrtcCreateTokenRequest request = WebrtcCreateTokenRequest.builder().reservationId(reservationId).build();

        // when
        Throwable thrown = catchThrowable(() -> webrtcService.getToken(request, userId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", WEBRTC_NO_PUBLISHER);

        // verify
        verify(webrtcSessionService, times(1)).createToken(any(), any());
    }

    @Test
    @DisplayName("토큰 삭제 성공")
    void 토큰_삭제_성공() throws Exception {
        // given
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        when(webrtcSessionService.getSession(reservationId)).thenReturn(Optional.of(mockSession));
        when(webrtcSessionService.removeToken(reservationId, "test-token", USER))
                .thenReturn(Optional.of("test-token"));

        WebrtcRemoveTokenRequest request = WebrtcRemoveTokenRequest.builder()
                .reservationId(reservationId)
                .token("test-token")
                .build();

        // when
        SuccessResponse<NoneResponse> response = webrtcService.removeToken(request, userId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(REMOVE_TOKEN_SUCCESS);

        // verify
        verify(webrtcSessionService, times(1)).removeToken(any(),any(),any());
    }

    @Test
    @DisplayName("토큰 삭제 실패 - 권한 없는 예약")
    void 토큰_삭제_실패__권한_없는_예약() throws Exception {
        // given
        Long invalidUserId = 100L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(invalidUserId);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);

        WebrtcRemoveTokenRequest request = WebrtcRemoveTokenRequest.builder()
                .reservationId(reservationId)
                .token("test-token")
                .build();

        // when
        Throwable thrown = catchThrowable(() -> webrtcService.removeToken(request, userId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ACCESS_DENIED);

        // verify
        verify(webrtcSessionService, never()).removeToken(any(), any(), any());
    }

    @Test
    @DisplayName("토큰 삭제 실패 - 없는 예약 번호")
    void 토큰_삭제_실패__없는_예약_번호() throws Exception {
        // given
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        WebrtcRemoveTokenRequest request = WebrtcRemoveTokenRequest.builder()
                .reservationId(reservationId)
                .token("test-token")
                .build();

        // when
        Throwable thrown = catchThrowable(() -> webrtcService.removeToken(request, userId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_NOT_FOUND);

        // verify
        verify(webrtcSessionService, never()).createToken(any(), any());
    }

    @Test
    @DisplayName("토큰 삭제 실패 - 확정된 예약이 아닌 경우")
    void 토큰_삭제_실패__확정된_예약이_아닌_경우() throws Exception {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getStatus()).thenReturn(ReservationStatus.APPLYED);

        WebrtcRemoveTokenRequest request = WebrtcRemoveTokenRequest.builder()
                .reservationId(reservationId)
                .token("test-token")
                .build();

        // when
        Throwable thrown = catchThrowable(() -> webrtcService.removeToken(request, userId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_STATUS_NOT_CONFIRMED);

        // verify
        verify(webrtcSessionService, never()).createToken(any(), any());
    }

    @Test
    @DisplayName("토큰 삭제 실패 - 취소된 예약인 경우")
    void 토큰_삭제_실패__취소된_예약인_경우() throws Exception {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getStatus()).thenReturn(ReservationStatus.CANCELLED);

        WebrtcRemoveTokenRequest request = WebrtcRemoveTokenRequest.builder()
                .reservationId(reservationId)
                .token("test-token")
                .build();

        // when
        Throwable thrown = catchThrowable(() -> webrtcService.removeToken(request, userId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_STATUS_NOT_CONFIRMED);

        // verify
        verify(webrtcSessionService, never()).createToken(any(), any());
    }

    @Test
    @DisplayName("토큰 삭제 실패 - 없는 세션")
    void 토큰_삭제_실패__없는_세션() throws Exception {
        // given
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        when(webrtcSessionService.getSession(reservationId)).thenReturn(Optional.empty());

        WebrtcRemoveTokenRequest request = WebrtcRemoveTokenRequest.builder()
                .reservationId(reservationId)
                .token("test-token")
                .build();

        // when
        Throwable thrown = catchThrowable(() ->webrtcService.removeToken(request, userId));

        // then
        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", WEBRTC_SESSION_UNOPENED);

        // verify
        verify(webrtcSessionService, never()).removeToken(any(), any(), any());
    }
}