package com.bangguddle.ownbang.domain.reservation.service;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.entity.AgentWorkhour;
import com.bangguddle.ownbang.domain.agent.repository.AgentRepository;
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
    private AgentRepository agentRepository;
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
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = mock(ReservationRequest.class);
        Room room = mock(Room.class);
        User user = mock(User.class);
        Reservation reservation = mock(Reservation.class);

        when(request.roomId()).thenReturn(1L);
        when(request.reservationTime()).thenReturn(now);
        when(request.status()).thenReturn(ReservationStatus.APPLYED);

        when(reservationRepository.findByRoomIdAndTimeWithLock(anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        when(reservationRepository.findByRoomIdAndUserIdAndStatusNot(anyLong(), anyLong(), any(ReservationStatus.class)))
                .thenReturn(Optional.empty());
        when(roomRepository.getById(anyLong())).thenReturn(room);
        when(userRepository.getById(anyLong())).thenReturn(user);
        when(request.toEntity(any(Room.class), any(User.class))).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        SuccessResponse<NoneResponse> response = reservationService.createReservation(userId, request);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_MAKE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예약 신청 실패 - 중복된 예약")
    void createReservation_Fail_DuplicatedReservation() {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, now, ReservationStatus.APPLYED);
        Room room = mock(Room.class);
        User user = mock(User.class);
        Reservation reservation = request.toEntity(room, user);

        when(reservationRepository.findByRoomIdAndTimeWithLock(anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(reservation));

        AppException exception = assertThrows(AppException.class, () -> reservationService.createReservation(userId,request));
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
        Long userId = 1L;
        Long reservationId = 1L;
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);
        Reservation reservation = mock(Reservation.class);
        when(reservation.getUser()).thenReturn(user);
        when(reservation.getStatus()).thenReturn(ReservationStatus.APPLYED);
        Reservation cancelledReservation = mock(Reservation.class);

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservation.withStatus()).thenReturn(cancelledReservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(cancelledReservation);

        SuccessResponse<NoneResponse> response = reservationService.updateStatusReservation(userId, reservationId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_UPDATE_STATUS_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예약 철회 실패 - 이미 취소된 예약")
    void updateStatusReservation_Fail_AlreadyCancelled() {
        Long reservationId = 1L;
        Long userId = 1L;
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);
        Reservation reservation = mock(Reservation.class);
        when(reservation.getUser()).thenReturn(user);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CANCELLED);

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        AppException exception = assertThrows(AppException.class, () -> reservationService.updateStatusReservation(userId, reservationId));
        assertThat(exception.getErrorCode()).isEqualTo(RESERVATION_CANCELLED_DUPLICATED);
    }

    @Test
    @DisplayName("예약 확정 성공")
    void confirmStatusReservation_Success() {
        Long userId = 1L;
        Long reservationId = 1L;
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);
        Reservation reservation = mock(Reservation.class);
        when(reservation.getUser()).thenReturn(user);
        when(reservation.getStatus()).thenReturn(ReservationStatus.APPLYED);
        Reservation confirmedReservation = mock(Reservation.class);

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservation.confirmStatus()).thenReturn(confirmedReservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(confirmedReservation);
        when(reservationRepository.existsByRoomAndReservationTimeAndStatus(any(), any(), any()))
                .thenReturn(Optional.empty());

        SuccessResponse<NoneResponse> response = reservationService.confirmStatusReservation(userId, reservationId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_CONFIRM_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }
    @Test
    @DisplayName("예약 확정 실패 - 같은 시간, 같은 방에 이미 확정된 예약 존재")
    void confirmStatusReservation_Fail_DuplicateTimeAndRoom() {
        Long reservationId = 1L;
        Long userId = 1L;
        Room room = mock(Room.class);
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);
        LocalDateTime reservationTime = LocalDateTime.now();
        Reservation reservation = mock(Reservation.class);
        when(reservation.getUser()).thenReturn(user);
        when(reservation.getStatus()).thenReturn(ReservationStatus.APPLYED);
        when(reservation.getRoom()).thenReturn(room);
        when(reservation.getReservationTime()).thenReturn(reservationTime);

        Reservation existingReservation = mock(Reservation.class);
        when(existingReservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.existsByRoomAndReservationTimeAndStatus(room, reservationTime, ReservationStatus.CONFIRMED))
                .thenReturn(Optional.of(existingReservation));

        AppException exception = assertThrows(AppException.class,
                () -> reservationService.confirmStatusReservation(userId, reservationId));
        assertEquals(RESERVATION_CONFIRMED_DUPLICATED_TIME_ROOM, exception.getErrorCode());
    }
    @Test
    @DisplayName("예약 확정 실패 - 이미 확정된 예약")
    void confirmStatusReservation_Fail_AlreadyConfirmed() {
        Long userId = 1L;
        Long reservationId = 1L;
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);
        Reservation reservation = mock(Reservation.class);
        when(reservation.getUser()).thenReturn(user);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        AppException exception = assertThrows(AppException.class,
                () -> reservationService.confirmStatusReservation(userId, reservationId));
        assertThat(exception.getErrorCode()).isEqualTo(RESERVATION_CONFIRMED_DUPLICATED);
    }

    @Test
    @DisplayName("예약 확정 실패 - 취소된 예약")
    void confirmStatusReservation_Fail_CancelledReservation() {
        Long userId = 1L;
        Long reservationId = 1L;
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);
        Reservation reservation = mock(Reservation.class);
        when(reservation.getUser()).thenReturn(user);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CANCELLED);

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        AppException exception = assertThrows(AppException.class,
                () -> reservationService.confirmStatusReservation(userId, reservationId));
        assertThat(exception.getErrorCode()).isEqualTo(RESERVATION_CONFIRMED_UNAVAILABLE);
    }
    @Test
    @DisplayName("중개인 예약 목록 조회 성공 - 시간순, ID순 정렬 및 상태 변경 확인")
    void getAgentReservations_Success() {
        Long agentId = 1L;
        Long userId = 1L;
        LocalDateTime baseTime = LocalDateTime.of(2023, 1, 1, 0, 0);

        Agent agent = mock(Agent.class);
        when(agent.getId()).thenReturn(agentId);

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        when(agentRepository.getByUserId(userId)).thenReturn(agent);

        Room room1 = mock(Room.class);
        Room room2 = mock(Room.class);
        Room room3 = mock(Room.class);
        when(room1.getId()).thenReturn(1L);
        when(room2.getId()).thenReturn(2L);
        when(room3.getId()).thenReturn(3L);

        Reservation reservation1 = mock(Reservation.class);
        Reservation reservation2 = mock(Reservation.class);
        Reservation reservation3 = mock(Reservation.class);

        when(reservation1.getId()).thenReturn(1L);
        when(reservation2.getId()).thenReturn(2L);
        when(reservation3.getId()).thenReturn(3L);
        when(reservation1.getReservationTime()).thenReturn(baseTime);
        when(reservation2.getReservationTime()).thenReturn(baseTime);
        when(reservation3.getReservationTime()).thenReturn(baseTime.plusDays(1));
        when(reservation1.getStatus()).thenReturn(ReservationStatus.APPLYED);
        when(reservation2.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        when(reservation3.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        when(reservation1.getRoom()).thenReturn(room1);
        when(reservation2.getRoom()).thenReturn(room2);
        when(reservation3.getRoom()).thenReturn(room3);
        when(reservation1.getUser()).thenReturn(user);
        when(reservation2.getUser()).thenReturn(user);
        when(reservation3.getUser()).thenReturn(user);

        List<Reservation> reservationList = Arrays.asList(reservation1, reservation2, reservation3);
        when(reservationRepository.findByRoomAgentIdAndReservationTimeAfterOrderByReservationTimeAscIdAsc(eq(agentId), any(LocalDateTime.class)))
                .thenReturn(reservationList);

        Video video2 = mock(Video.class);
        Video video3 = mock(Video.class);
        when(video2.getVideoStatus()).thenReturn(VideoStatus.RECORDED);
        when(video3.getVideoStatus()).thenReturn(VideoStatus.RECORDING);

        when(videoRepository.findByReservationId(2L)).thenReturn(Optional.of(video2));
        when(videoRepository.findByReservationId(3L)).thenReturn(Optional.of(video3));

        Reservation completedReservation2 = mock(Reservation.class);
        when(completedReservation2.getId()).thenReturn(2L);
        when(completedReservation2.getReservationTime()).thenReturn(baseTime);
        when(completedReservation2.getStatus()).thenReturn(ReservationStatus.COMPLETED);
        when(completedReservation2.getRoom()).thenReturn(room2);
        when(completedReservation2.getUser()).thenReturn(user);

        when(reservation2.completeStatus()).thenReturn(completedReservation2);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(completedReservation2);

        SuccessResponse<ReservationListResponse> response = reservationService.getAgentReservations(userId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_LIST_SUCCESS);
        assertThat(response.data().reservations()).hasSize(3);

        List<ReservationResponse> reservations = response.data().reservations();

        assertThat(reservations.get(0).id()).isEqualTo(1L);
        assertThat(reservations.get(1).id()).isEqualTo(2L);
        assertThat(reservations.get(2).id()).isEqualTo(3L);

        assertThat(reservations.get(0).reservationTime()).isEqualTo(baseTime);
        assertThat(reservations.get(1).reservationTime()).isEqualTo(baseTime);
        assertThat(reservations.get(2).reservationTime()).isEqualTo(baseTime.plusDays(1));

        assertThat(reservations.get(0).status()).isEqualTo(ReservationStatus.APPLYED);
        assertThat(reservations.get(1).status()).isEqualTo(ReservationStatus.COMPLETED);
        assertThat(reservations.get(2).status()).isEqualTo(ReservationStatus.CONFIRMED);

        verify(agentRepository).getByUserId(userId);
        verify(reservationRepository).findByRoomAgentIdAndReservationTimeAfterOrderByReservationTimeAscIdAsc(eq(agentId), any(LocalDateTime.class));
        verify(videoRepository, times(2)).findByReservationId(anyLong());
        verify(reservationRepository).save(any(Reservation.class));
    }
    @Test
    @DisplayName("중개인 예약 목록 조회 - 빈 목록")
    void getAgentReservations_Empty() {
        Long userId = 1L;
        Long agentId = 1L;

        Agent agent = mock(Agent.class);
        when(agent.getId()).thenReturn(agentId);

        when(agentRepository.getByUserId(userId)).thenReturn(agent);

        when(reservationRepository.findByRoomAgentIdAndReservationTimeAfterOrderByReservationTimeAscIdAsc(eq(agentId), any(LocalDateTime.class)))
                .thenReturn(List.of());

        SuccessResponse<ReservationListResponse> response = reservationService.getAgentReservations(userId);

        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_LIST_EMPTY);
        assertThat(response.data().reservations()).isEmpty();

        verify(agentRepository).getByUserId(userId);
        verify(reservationRepository).findByRoomAgentIdAndReservationTimeAfterOrderByReservationTimeAscIdAsc(eq(agentId), any(LocalDateTime.class));
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
