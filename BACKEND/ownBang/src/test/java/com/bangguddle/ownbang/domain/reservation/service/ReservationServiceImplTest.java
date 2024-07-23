package com.bangguddle.ownbang.domain.reservation.service;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationRequest;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

        // Mock 설정
        when(reservationRepository.findByRoomIdAndTimeWithLock(any(), any())).thenReturn(Optional.empty());
        when(reservationRepository.findByRoomIdAndUserIdAndStatusNot(any(), any(), any())).thenReturn(Optional.empty());

        // 예약 생성
        SuccessResponse<NoneResponse> response = reservationService.createReservation(request);

        // 검증
        assertNotNull(response);
        assertEquals(SuccessCode.RESERVATION_MAKE_SUCCESS, response.successCode());
        assertEquals(NoneResponse.NONE, response.data());

        // save 메소드 호출 검증
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_DuplicateReservation() {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, 1L, now, ReservationStatus.예약신청);

        // Mock 설정
        when(reservationRepository.findByRoomIdAndTimeWithLock(any(), any())).thenReturn(Optional.of(
                Reservation.builder()
                        .roomId(1L)
                        .userId(1L)
                        .time(now)
                        .status(ReservationStatus.예약신청)
                        .build()
        ));

        // 예약 생성 시 예외 검증
        AppException exception = assertThrows(AppException.class, () -> reservationService.createReservation(request));
        assertEquals(ErrorCode.Reservation_DUPLICATED, exception.getErrorCode());
    }

    @Test
    void createReservation_UserAlreadyReserved() {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, 1L, now, ReservationStatus.예약신청);

        // Mock 설정
        when(reservationRepository.findByRoomIdAndTimeWithLock(any(), any())).thenReturn(Optional.empty());
        when(reservationRepository.findByRoomIdAndUserIdAndStatusNot(any(), any(), any())).thenReturn(Optional.of(
                Reservation.builder()
                        .roomId(1L)
                        .userId(1L)
                        .time(now)
                        .status(ReservationStatus.예약신청)
                        .build()
        ));

        // 예약 생성 시 예외 검증
        AppException exception = assertThrows(AppException.class, () -> reservationService.createReservation(request));
        assertEquals(ErrorCode.Reservation_COMPLETED, exception.getErrorCode());
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

        // Mock 설정
        when(reservationRepository.findByUserId(userId)).thenReturn(Arrays.asList(reservation1, reservation2));

        // 예약 목록 조회
        SuccessResponse<ReservationListResponse> response = reservationService.getReservationsByUserId(userId);

        // 검증
        assertNotNull(response);
        assertEquals(SuccessCode.RESERVATION_LIST_SUCCESS, response.successCode());
        assertNotNull(response.data());
        assertEquals(2, response.data().reservations().size());
        assertEquals(1L, response.data().reservations().get(0).getId());
        assertEquals(2L, response.data().reservations().get(1).getId());
        assertEquals(ReservationStatus.예약신청, response.data().reservations().get(0).getStatus());
        assertEquals(ReservationStatus.예약확정, response.data().reservations().get(1).getStatus());
    }
}
