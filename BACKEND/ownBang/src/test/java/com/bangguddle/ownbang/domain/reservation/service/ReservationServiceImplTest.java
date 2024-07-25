package com.bangguddle.ownbang.domain.reservation.service;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationRequest;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.reservation.service.impl.ReservationServiceImpl;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void createReservation_Success() {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, 1L, now, ReservationStatus.예약신청);

        when(reservationRepository.findByRoomIdAndTimeWithLock(any(), any())).thenReturn(Optional.empty());
        when(reservationRepository.findByRoomIdAndUserIdAndStatusNot(any(), any(), any())).thenReturn(Optional.empty());

        SuccessResponse<NoneResponse> response = reservationService.createReservation(request);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(SuccessCode.RESERVATION_MAKE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_DuplicateReservation() {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, 1L, now, ReservationStatus.예약신청);

        when(reservationRepository.findByRoomIdAndTimeWithLock(any(), any())).thenReturn(Optional.of(
                Reservation.builder()
                        .roomId(1L)
                        .userId(1L)
                        .time(now)
                        .status(ReservationStatus.예약신청)
                        .build()
        ));

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.RESERVATION_DUPLICATED);
    }

    @Test
    void createReservation_UserAlreadyReserved() {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, 1L, now, ReservationStatus.예약신청);

        when(reservationRepository.findByRoomIdAndTimeWithLock(any(), any())).thenReturn(Optional.empty());
        when(reservationRepository.findByRoomIdAndUserIdAndStatusNot(any(), any(), any())).thenReturn(Optional.of(
                Reservation.builder()
                        .roomId(1L)
                        .userId(1L)
                        .time(now)
                        .status(ReservationStatus.예약신청)
                        .build()
        ));

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.RESERVATION_COMPLETED);
    }

    @Test
    void getReservationsByUserId_Success() {
        long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation1 = Reservation.builder()
                .id(1L)
                .roomId(1L)
                .userId(userId)
                .time(now)
                .status(ReservationStatus.예약신청)
                .build();
        Reservation reservation2 = Reservation.builder()
                .id(2L)
                .roomId(2L)
                .userId(userId)
                .time(now.plusDays(1))
                .status(ReservationStatus.예약확정)
                .build();

        when(reservationRepository.findByUserId(userId)).thenReturn(Arrays.asList(reservation1, reservation2));

        SuccessResponse<ReservationListResponse> response = reservationService.getMyReservationList(userId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(SuccessCode.RESERVATION_LIST_SUCCESS);
        assertThat(response.data()).isNotNull();
        assertThat(response.data().reservations()).hasSize(2);
        assertThat(response.data().reservations().get(0).getId()).isEqualTo(1L);
        assertThat(response.data().reservations().get(1).getId()).isEqualTo(2L);
        assertThat(response.data().reservations().get(0).getStatus()).isEqualTo(ReservationStatus.예약신청);
        assertThat(response.data().reservations().get(1).getStatus()).isEqualTo(ReservationStatus.예약확정);
    }

}
