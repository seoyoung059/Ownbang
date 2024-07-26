package com.bangguddle.ownbang.domain.reservation.service;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationRequest;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.reservation.service.impl.ReservationServiceImpl;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    @DisplayName("새 예약 생성 - 성공")
    void createReservation_Success() {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, 1L, now, ReservationStatus.APPLYED);

        when(reservationRepository.findByRoomIdAndTimeWithLock(any(), any())).thenReturn(Optional.empty());
        when(reservationRepository.findByRoomIdAndUserIdAndStatusNot(any(), any(), any())).thenReturn(Optional.empty());

        SuccessResponse<NoneResponse> response = reservationService.createReservation(request);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(SuccessCode.RESERVATION_MAKE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("중복 예약 시 예외 발생")
    void createReservation_DuplicateReservation() {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, 1L, now, ReservationStatus.APPLYED);

        when(reservationRepository.findByRoomIdAndTimeWithLock(any(), any())).thenReturn(Optional.of(
                Reservation.builder()
                        .roomId(1L)
                        .userId(1L)
                        .reservationTime(now)
                        .status(ReservationStatus.APPLYED)
                        .build()
        ));

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.RESERVATION_DUPLICATED);
    }

    @Test
    @DisplayName("사용자가 이미 예약한 경우 예외 발생")
    void createReservation_UserAlreadyReserved() {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, 1L, now, ReservationStatus.APPLYED);

        when(reservationRepository.findByRoomIdAndTimeWithLock(any(), any())).thenReturn(Optional.empty());
        when(reservationRepository.findByRoomIdAndUserIdAndStatusNot(any(), any(), any())).thenReturn(Optional.of(
                Reservation.builder()
                        .roomId(1L)
                        .userId(1L)
                        .reservationTime(now)
                        .status(ReservationStatus.APPLYED)
                        .build()
        ));

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.RESERVATION_COMPLETED);
    }

    @Test
    @DisplayName("사용자의 예약 목록 조회 - 성공")
    void getReservationsByUserId_Success() {
        long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation1 = Reservation.builder()
                .id(1L)
                .roomId(1L)
                .userId(userId)
                .reservationTime(now)
                .status(ReservationStatus.APPLYED)
                .build();
        Reservation reservation2 = Reservation.builder()
                .id(2L)
                .roomId(2L)
                .userId(userId)
                .reservationTime(now.plusDays(1))
                .status(ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findByUserId(userId)).thenReturn(Arrays.asList(reservation1, reservation2));

        SuccessResponse<ReservationListResponse> response = reservationService.getMyReservationList(userId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(SuccessCode.RESERVATION_LIST_SUCCESS);
        assertThat(response.data()).isNotNull();
        assertThat(response.data().reservations()).hasSize(2);
        assertThat(response.data().reservations().get(0).getId()).isEqualTo(1L);
        assertThat(response.data().reservations().get(1).getId()).isEqualTo(2L);
        assertThat(response.data().reservations().get(0).getStatus()).isEqualTo(ReservationStatus.APPLYED);
        assertThat(response.data().reservations().get(1).getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
    }

    @Test
    @DisplayName("예약 철회 성공 - 상태를 예약취소로 변경 후 저장")
    void updateStatusReservation_Success() {
        Long id = 1L;
        Reservation reservation = Reservation.builder()
                .id(id)
                .roomId(101L)
                .userId(1L)
                .reservationTime(LocalDateTime.now())
                .status(ReservationStatus.APPLYED)
                .build();

        Reservation updatedReservation = reservation.withStatus();

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);

        SuccessResponse<NoneResponse> response = reservationService.updateStatusReservation(id);

        assertEquals(SuccessCode.RESERVATION_UPDATE_STATUS_SUCCESS, response.successCode());
        assertEquals(NoneResponse.NONE, response.data());

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(captor.capture());
        Reservation capturedReservation = captor.getValue();

        assertEquals(ReservationStatus.CANCELED, capturedReservation.getStatus());
    }


    @Test
    @DisplayName("예약 상태 업데이트 실패 - 예약 ID가 유효하지 않음")
    void updateStatusReservation_Fail_InvalidId() {
        Long id = 1L;

        when(reservationRepository.findById(id)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> reservationService.updateStatusReservation(id));

        assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예약 상태 업데이트 실패 - 이미 취소된 예약")
    void updateStatusReservation_Fail_AlreadyCanceled() {
        Long id = 1L;
        Reservation reservation = Reservation.builder()
                .id(id)
                .roomId(101L)
                .userId(1L)
                .reservationTime(LocalDateTime.now())
                .status(ReservationStatus.CANCELED)
                .build();

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        AppException exception = assertThrows(AppException.class, () -> reservationService.updateStatusReservation(id));

        assertEquals(ErrorCode.RESERVATION_CANCELED_DUPLICATED, exception.getErrorCode());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예약 상태 업데이트 실패 - 확정된 예약")
    void updateStatusReservation_Fail_Confirmed() {
        Long id = 1L;
        Reservation reservation = Reservation.builder()
                .id(id)
                .roomId(101L)
                .userId(1L)
                .reservationTime(LocalDateTime.now())
                .status(ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        AppException exception = assertThrows(AppException.class, () -> reservationService.updateStatusReservation(id));

        assertEquals(ErrorCode.RESERVATION_CANCELED_UNAVAILABLE, exception.getErrorCode());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }
    @Test
    @DisplayName("예약 확정 성공 - 상태를 예약확정으로 변경 후 저장")
    void confirmStatusReservation_Success() {
        Long id = 1L;
        Reservation reservation = Reservation.builder()
                .id(id)
                .roomId(101L)
                .userId(1L)
                .reservationTime(LocalDateTime.now())
                .status(ReservationStatus.APPLYED)
                .build();

        Reservation updatedReservation = reservation.confirmStatus();

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);

        SuccessResponse<NoneResponse> response = reservationService.confirmStatusReservation(id);

        assertEquals(SuccessCode.RESERVATION_CONFIRM_SUCCESS, response.successCode());
        assertEquals(NoneResponse.NONE, response.data());

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(captor.capture());
        Reservation capturedReservation = captor.getValue();

        assertEquals(ReservationStatus.CONFIRMED, capturedReservation.getStatus());
    }


    @Test
    @DisplayName("중개인 예약 확정 실패 - 예약 ID가 유효하지 않음")
    void confirmStatusReservation_Fail_InvalidId() {
        Long id = 1L;

        when(reservationRepository.findById(id)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> reservationService.confirmStatusReservation(id));

        assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    @DisplayName("중개인 예약 확정 실패 -  취소된 예약일 경우")
    void confirmStatusReservation_Fail_AlreadyCanceled() {
        Long id = 1L;
        Reservation reservation = Reservation.builder()
                .id(id)
                .roomId(101L)
                .userId(1L)
                .reservationTime(LocalDateTime.now())
                .status(ReservationStatus.CANCELED)
                .build();

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        AppException exception = assertThrows(AppException.class, () -> reservationService.confirmStatusReservation(id));

        assertEquals(ErrorCode.RESERVATION_CONFIRMED_UNAVAILABLE, exception.getErrorCode());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    @DisplayName("중개인 예약 확정 실패 - 이미 확정된 예약")
    void confirmStatusReservation_Fail_Confirmed() {
        Long id = 1L;
        Reservation reservation = Reservation.builder()
                .id(id)
                .roomId(101L)
                .userId(1L)
                .reservationTime(LocalDateTime.now())
                .status(ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        AppException exception = assertThrows(AppException.class, () -> reservationService.confirmStatusReservation(id));

        assertEquals(ErrorCode.RESERVATION_CONFIRMED_DUPLICATED, exception.getErrorCode());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }



}

