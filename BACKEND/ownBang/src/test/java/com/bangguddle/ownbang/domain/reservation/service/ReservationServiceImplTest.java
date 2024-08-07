package com.bangguddle.ownbang.domain.reservation.service;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.entity.AgentWorkhour;
import com.bangguddle.ownbang.domain.agent.repository.AgentWorkhourRepository;
import com.bangguddle.ownbang.domain.reservation.dto.*;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.reservation.service.impl.ReservationServiceImpl;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.domain.video.entity.Video;
import com.bangguddle.ownbang.domain.video.entity.VideoStatus;
import com.bangguddle.ownbang.domain.video.repository.VideoRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Mock
    private AgentWorkhourRepository agentWorkhourRepository;

    @Mock
    private VideoRepository videoRepository;

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
    @DisplayName("예약 목록 조회 성공 (비디오 상태 포함)")
    void getMyReservationList_SuccessWithVideoStatus() {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation1 = Reservation.builder().id(1L).room(Room.builder().build()).user(User.builder().build()).reservationTime(now).status(ReservationStatus.APPLYED).build();
        Reservation reservation2 = Reservation.builder().id(2L).room(Room.builder().build()).user(User.builder().build()).reservationTime(now.plusDays(1)).status(ReservationStatus.CONFIRMED).build();

        when(reservationRepository.findByUserId(anyLong())).thenReturn(List.of(reservation1, reservation2));
        when(videoRepository.findByReservationId(1L)).thenReturn(Optional.empty());
        when(videoRepository.findByReservationId(2L)).thenReturn(Optional.of(new Video(reservation2, "url", VideoStatus.RECORDED)));

        SuccessResponse<ReservationListResponse> response = reservationService.getMyReservationList(userId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_LIST_SUCCESS);
        assertThat(response.data().reservations().size()).isEqualTo(2);

        List<ReservationResponse> expectedResponses = List.of(
                ReservationResponse.from(reservation1),
                ReservationResponse.from(reservation2.completeStatus())
        );

        assertThat(response.data().reservations())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedResponses);

        // 추가적으로 상태 변경을 명시적으로 확인
        assertThat(response.data().reservations().get(0).status()).isEqualTo(ReservationStatus.APPLYED);
        assertThat(response.data().reservations().get(1).status()).isEqualTo(ReservationStatus.COMPLETED);
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
    @DisplayName("예약 확정 실패 - 같은 시간, 같은 방에 이미 확정된 예약 존재")
    void confirmStatusReservation_Fail_DuplicateTimeAndRoom() {
        Long reservationId = 1L;
        Room room = mock(Room.class);
        LocalDateTime reservationTime = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .id(reservationId)
                .status(ReservationStatus.APPLYED)
                .room(room)
                .reservationTime(reservationTime)
                .build();

        Reservation existingReservation = Reservation.builder()
                .id(2L)
                .status(ReservationStatus.CONFIRMED)
                .room(room)
                .reservationTime(reservationTime)
                .build();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.existsByRoomAndReservationTimeAndStatus(room, reservationTime, ReservationStatus.CONFIRMED))
                .thenReturn(Optional.of(existingReservation));

        AppException exception = assertThrows(AppException.class,
                () -> reservationService.confirmStatusReservation(reservationId));
        assertEquals(RESERVATION_CONFIRMED_DUPLICATED_TIME_ROOM, exception.getErrorCode());
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
    @Test
    @DisplayName("중개인 예약 목록 조회 성공 - 시간순, ID순 정렬 및 상태 변경 확인")
    void getAgentReservations_Success() {
        // Given
        Long agentId = 1L;
        LocalDateTime baseTime = LocalDateTime.of(2023, 1, 1, 0, 0);

        Room room = mock(Room.class);
        User user = mock(User.class);

        Reservation reservation1 = new Reservation(1L, room, user, baseTime, ReservationStatus.APPLYED);
        Reservation reservation2 = new Reservation(2L, room, user, baseTime, ReservationStatus.CONFIRMED);
        Reservation reservation3 = new Reservation(3L, room, user, baseTime.plusDays(1), ReservationStatus.CONFIRMED);

        when(reservationRepository.findByRoomAgentIdAndReservationTimeAfterOrderByReservationTimeAscIdAsc(eq(agentId), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(reservation1, reservation2, reservation3));

        when(videoRepository.findByReservationId(2L)).thenReturn(Optional.of(new Video(reservation2, "url", VideoStatus.RECORDED)));
        when(videoRepository.findByReservationId(3L)).thenReturn(Optional.of(new Video(reservation3, "url", VideoStatus.RECORDING)));

        Reservation completedReservation2 = new Reservation(2L, room, user, baseTime, ReservationStatus.COMPLETED);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(completedReservation2);

        // When
        SuccessResponse<ReservationListResponse> response = reservationService.getAgentReservations(agentId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_LIST_SUCCESS);
        assertThat(response.data().reservations()).hasSize(3);

        List<ReservationResponse> reservations = response.data().reservations();

        // 시간순, ID순 정렬 확인
        assertThat(reservations.get(0).id()).isEqualTo(1L);
        assertThat(reservations.get(1).id()).isEqualTo(2L);
        assertThat(reservations.get(2).id()).isEqualTo(3L);

        assertThat(reservations.get(0).reservationTime()).isEqualTo(baseTime);
        assertThat(reservations.get(1).reservationTime()).isEqualTo(baseTime);
        assertThat(reservations.get(2).reservationTime()).isEqualTo(baseTime.plusDays(1));

        // 상태 변경 확인
        assertThat(reservations.get(0).status()).isEqualTo(ReservationStatus.APPLYED);
        assertThat(reservations.get(1).status()).isEqualTo(ReservationStatus.COMPLETED);
        assertThat(reservations.get(2).status()).isEqualTo(ReservationStatus.CONFIRMED);

        verify(reservationRepository).findByRoomAgentIdAndReservationTimeAfterOrderByReservationTimeAscIdAsc(eq(agentId), any(LocalDateTime.class));
        verify(videoRepository, times(2)).findByReservationId(anyLong());
        verify(reservationRepository).save(any(Reservation.class));
    }
    @Test
    @DisplayName("중개인 예약 목록 조회 - 빈 목록")
    void getAgentReservations_Empty() {
        Long agentId = 1L;

        when(reservationRepository.findByRoomAgentIdAndReservationTimeAfterOrderByReservationTimeAscIdAsc(eq(agentId), any(LocalDateTime.class)))
                .thenReturn(List.of());

        SuccessResponse<ReservationListResponse> response = reservationService.getAgentReservations(agentId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_LIST_EMPTY);
        assertThat(response.data().reservations()).isEmpty();
    }
    @Test
    @DisplayName("예약 가능 시간 조회 성공")
    void getAvailableTimes_Success() {
        // Given
        Long roomId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
        AvailableTimeRequest request = new AvailableTimeRequest(roomId, date);

        Room room = mock(Room.class);
        Agent agent = mock(Agent.class);
        when(room.getAgent()).thenReturn(agent);

        AgentWorkhour workhour = mock(AgentWorkhour.class);
        when(workhour.getStartTime()).thenReturn("09:00");
        when(workhour.getEndTime()).thenReturn("18:00");

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(agentWorkhourRepository.findByAgentAndDay(any(Agent.class), any(AgentWorkhour.Day.class)))
                .thenReturn(workhour);
        when(reservationRepository.findConfirmedReservationTimes(eq(roomId), eq(date)))
                .thenReturn(List.of(LocalTime.of(10, 0), LocalTime.of(14, 30)));

        // When
        SuccessResponse<AvailableTimeResponse> response = reservationService.getAvailableTimes(request);

        // Then
        assertThat(response.successCode()).isEqualTo(AVAILABLE_TIMES_RETRIEVED);
        assertThat(response.data().availableTimes()).hasSize(16); // 9:00부터 17:30까지, 10:00와 14:30 제외
        assertThat(response.data().availableTimes()).contains("09:00", "09:30", "11:00", "14:00", "15:00", "17:30");
        assertThat(response.data().availableTimes()).doesNotContain("10:00", "14:30", "18:00");
    }

    @Test
    @DisplayName("존재하지 않는 매물로 조회 시 실패")
    void getAvailableTimes_RoomNotFound() {
        // Given
        Long roomId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
        AvailableTimeRequest request = new AvailableTimeRequest(roomId, date);

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(AppException.class, () -> reservationService.getAvailableTimes(request));
    }


}
