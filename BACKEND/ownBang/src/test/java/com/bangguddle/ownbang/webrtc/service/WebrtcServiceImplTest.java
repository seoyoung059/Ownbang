package com.bangguddle.ownbang.webrtc.service;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcCreateRequest;
import com.bangguddle.ownbang.domain.webrtc.service.impl.WebrtcServiceImpl;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;

import io.openvidu.java.client.Connection;
import io.openvidu.java.client.ConnectionProperties;
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
public class WebrtcServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(WebrtcServiceImplTest.class);

    @Mock
    private OpenVidu openVidu;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private WebrtcServiceImpl webrtcService;

    @Test
    @DisplayName("[정상] 토큰 발급")
    void 토큰_발급() throws Exception {
        // given
        Long reservationId = 13l;
        WebrtcCreateRequest request = WebrtcCreateRequest.builder()
                .reservationId(reservationId)
                .build();

        Reservation reservation = mock(Reservation.class);
        Session mockSession = mock(Session.class);
        Connection mockConnection = mock(Connection.class);

        // mock
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(openVidu.createSession()).thenReturn(mockSession);
        when(mockSession.createConnection(any(ConnectionProperties.class))).thenReturn(mockConnection);
        when(mockConnection.getToken()).thenReturn("mockToken");

        // when
        SuccessResponse response = webrtcService.getToken(request);
        String token = (String) response.data();

        // then
        assertThat(token).isNotEmpty().isNotNull();
        assertThat(token).isEqualTo("mockToken");
    }

    @Test
    @DisplayName("[실패] 유효하지 않은 예약ID로 토큰 발급")
    void 실패_유효하지_않은_예약_ID로_토큰_발급() throws Exception {
        // given
        Long reservationId = 13l;
        WebrtcCreateRequest request = WebrtcCreateRequest.builder()
                .reservationId(reservationId)
                .build();

        // mock
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> webrtcService.getToken(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.RESERVATION_NOT_FOUND);

        // verify
        verify(reservationRepository, times(1)).findById(reservationId);
        verifyNoInteractions(openVidu);
    }

    @Test
    @DisplayName("[실패] 이미 발급된 토큰 - 중개인 중복")
    void 실패_이미_발급된_토큰_중개인_중복() throws Exception {
        // given


        // when


        // then

    }

    @Test
    @DisplayName("[실패] 이미 발급된 토큰 - 임차인 중복")
    void 실패_이미_발급된_토큰_임차인_중복() throws Exception {
        // given


        // when


        // then

    }

    @Test
    @DisplayName("[실패] 권한이 없는 예약 - 중개인 기준")
    void 실패_권한이_없는_예약() throws Exception {
        // given


        // when


        // then

    }

    @Test
    @DisplayName("[실패] 권한이 없는 예약 - 임차인 기준")
    void 실패_권한이_없는_예약_임차인_기준() throws Exception {
        // given


        // when


        // then

    }
}
