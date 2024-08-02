package com.bangguddle.ownbang.domain.reservation.service;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationRequest;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.reservation.service.impl.ReservationServiceImpl;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("예약 신청 성공")
    void createReservation_Success() {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, 1L, now, ReservationStatus.APPLYED);
        Room room = Room.builder().build();
        User user = User.builder().build();
        Reservation reservation = request.toEntity(room, user);

        when(reservationRepository.findByRoomIdAndTimeWithLock(anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        when(reservationRepository.findByRoomIdAndUserIdAndStatusNot(anyLong(), anyLong(), any(ReservationStatus.class)))
                .thenReturn(Optional.empty());
        when(roomRepository.getById(anyLong())).thenReturn(room);
        when(userRepository.getById(anyLong())).thenReturn(user);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        SuccessResponse<NoneResponse> response = reservationService.createReservation(request);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_MAKE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예약 신청 실패 - 중복된 예약")
    void createReservation_Fail_DuplicatedReservation() {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, 1L, now, ReservationStatus.APPLYED);
        Room room = Room.builder().build();
        User user = User.builder().build();
        Reservation reservation = request.toEntity(room, user);

        when(reservationRepository.findByRoomIdAndTimeWithLock(anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(reservation));

        AppException exception = assertThrows(AppException.class, () -> reservationService.createReservation(request));
        assertThat(exception.getErrorCode()).isEqualTo(RESERVATION_DUPLICATED);
    }

    @Test
    @DisplayName("예약 목록 조회 성공")
    void getMyReservationList_Success() {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation1 = Reservation.builder().room(Room.builder().build()).user(User.builder().build()).reservationTime(now).status(ReservationStatus.APPLYED).build();
        Reservation reservation2 = Reservation.builder().room(Room.builder().build()).user(User.builder().build()).reservationTime(now.plusDays(1)).status(ReservationStatus.CONFIRMED).build();

        when(reservationRepository.findByUserId(anyLong())).thenReturn(List.of(reservation1, reservation2));

        SuccessResponse<ReservationListResponse> response = reservationService.getMyReservationList(userId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_LIST_SUCCESS);
        assertThat(response.data().reservations().size()).isEqualTo(2);
        assertThat(response.data().reservations()).contains(reservation1, reservation2);
    }

    @Test
    @DisplayName("예약 목록 조회 성공 - 빈 목록")
    void getMyReservationList_Empty() {
        Long userId = 1L;

        when(reservationRepository.findByUserId(anyLong())).thenReturn(List.of());

        SuccessResponse<ReservationListResponse> response = reservationService.getMyReservationList(userId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_LIST_EMPTY);
        assertThat(response.data().reservations()).isEmpty();
    }

    @Test
    @DisplayName("예약 철회 성공")
    void updateStatusReservation_Success() {
        Long reservationId = 1L;
        Reservation reservation = Reservation.builder().id(reservationId).status(ReservationStatus.APPLYED).build();
        Reservation cancelledReservation = reservation.withStatus();

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(cancelledReservation);

        SuccessResponse<NoneResponse> response = reservationService.updateStatusReservation(reservationId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_UPDATE_STATUS_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예약 철회 실패 - 이미 취소된 예약")
    void updateStatusReservation_Fail_AlreadyCancelled() {
        Long reservationId = 1L;
        Reservation reservation = Reservation.builder().id(reservationId).status(ReservationStatus.CANCELLED).build();

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        AppException exception = assertThrows(AppException.class, () -> reservationService.updateStatusReservation(reservationId));
        assertThat(exception.getErrorCode()).isEqualTo(RESERVATION_CANCELLED_DUPLICATED);
    }

    @Test
    @DisplayName("예약 확정 성공")
    void confirmStatusReservation_Success() {
        Long reservationId = 1L;
        Reservation reservation = Reservation.builder().id(reservationId).status(ReservationStatus.APPLYED).build();
        Reservation confirmedReservation = reservation.confirmStatus();

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(confirmedReservation);

        SuccessResponse<NoneResponse> response = reservationService.confirmStatusReservation(reservationId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_CONFIRM_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예약 확정 실패 - 이미 확정된 예약")
    void confirmStatusReservation_Fail_AlreadyConfirmed() {
        Long reservationId = 1L;
        Reservation reservation = Reservation.builder().id(reservationId).status(ReservationStatus.CONFIRMED).build();

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        AppException exception = assertThrows(AppException.class, () -> reservationService.confirmStatusReservation(reservationId));
        assertThat(exception.getErrorCode()).isEqualTo(RESERVATION_CONFIRMED_DUPLICATED);
    }

    @Test
    @DisplayName("예약 확정 실패 - 취소된 예약")
    void confirmStatusReservation_Fail_CancelledReservation() {
        Long reservationId = 1L;
        Reservation reservation = Reservation.builder().id(reservationId).status(ReservationStatus.CANCELLED).build();

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        AppException exception = assertThrows(AppException.class, () -> reservationService.confirmStatusReservation(reservationId));
        assertThat(exception.getErrorCode()).isEqualTo(RESERVATION_CONFIRMED_UNAVAILABLE);
    }
}
