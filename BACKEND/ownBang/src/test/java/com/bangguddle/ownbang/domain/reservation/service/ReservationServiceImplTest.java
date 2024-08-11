package com.bangguddle.ownbang.domain.reservation.service;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.repository.AgentRepository;
import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;
import com.bangguddle.ownbang.domain.agent.workhour.repository.AgentWorkhourRepository;
import com.bangguddle.ownbang.domain.reservation.dto.*;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.reservation.service.impl.ReservationServiceImpl;
import com.bangguddle.ownbang.domain.review.entity.Review;
import com.bangguddle.ownbang.domain.review.repository.ReviewRepository;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.domain.video.entity.Video;
import com.bangguddle.ownbang.domain.video.entity.VideoStatus;
import com.bangguddle.ownbang.domain.video.repository.VideoRepository;
import com.bangguddle.ownbang.domain.webrtc.service.WebrtcSessionService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import io.openvidu.java.client.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private WebrtcSessionService webrtcSessionService;

    @Mock
    private AgentRepository agentRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private AgentWorkhourRepository agentWorkhourRepository;

    @Mock
    private VideoRepository videoRepository;

    @Test
    @DisplayName("예약 신청 성공")
    void createReservation_Success() {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, now, ReservationStatus.APPLYED);
        Room room = mock(Room.class);
        User user = mock(User.class);
        Reservation reservation = mock(Reservation.class);

        when(roomRepository.getById(anyLong())).thenReturn(room);
        when(userRepository.getById(anyLong())).thenReturn(user);
        when(reservationRepository.findByRoomIdAndTimeWithLock(anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        when(reservationRepository.findByRoomIdAndUserIdAndStatusNot(anyLong(), anyLong(), any(ReservationStatus.class)))
                .thenReturn(Optional.empty());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        SuccessResponse<NoneResponse> response = reservationService.createReservation(userId, request);

        assertThat(response.successCode()).isEqualTo(RESERVATION_MAKE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예약 신청 실패 - 중복된 예약")
    void createReservation_Fail_DuplicatedReservation() {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(1L, now, ReservationStatus.APPLYED);
        Reservation existingReservation = mock(Reservation.class);

        when(reservationRepository.findByRoomIdAndTimeWithLock(anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(existingReservation));

        assertThatThrownBy(() -> reservationService.createReservation(userId, request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_DUPLICATED);
    }

    @Test
    @DisplayName("임차인 예약 목록 조회 성공 (isReview, Entrance, agentId 포함)")
    void getMyReservationList_Success() {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Agent agent = mock(Agent.class);
        when(agent.getId()).thenReturn(100L);
        Room room = mock(Room.class);
        when(room.getAgent()).thenReturn(agent);
        User user = mock(User.class);
        Reservation reservation1 = mock(Reservation.class);
        Reservation reservation2 = mock(Reservation.class);

        when(reservation1.getId()).thenReturn(1L);
        when(reservation1.getRoom()).thenReturn(room);
        when(reservation1.getUser()).thenReturn(user);
        when(reservation1.getReservationTime()).thenReturn(now);
        when(reservation1.getStatus()).thenReturn(ReservationStatus.CONFIRMED);

        when(reservation2.getId()).thenReturn(2L);
        when(reservation2.getRoom()).thenReturn(room);
        when(reservation2.getUser()).thenReturn(user);
        when(reservation2.getReservationTime()).thenReturn(now.plusDays(1));
        when(reservation2.getStatus()).thenReturn(ReservationStatus.COMPLETED);

        when(reservationRepository.findByUserId(userId)).thenReturn(List.of(reservation1, reservation2));
        when(webrtcSessionService.getSession(1L)).thenReturn(Optional.of(mock(Session.class)));
        when(reviewRepository.findByReservationId(2L)).thenReturn(Optional.empty());

        SuccessResponse<UserReservationListResponse> response = reservationService.getMyReservationList(userId);

        assertThat(response.successCode()).isEqualTo(RESERVATION_LIST_SUCCESS);
        assertThat(response.data().userReservations()).hasSize(2);

        UserReservationResponse firstReservation = response.data().userReservations().get(0);
        assertThat(firstReservation.status()).isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(firstReservation.enstance()).isTrue();
        assertThat(firstReservation.agentId()).isEqualTo(100L);
        assertThat(firstReservation.isReview()).isFalse();

        UserReservationResponse secondReservation = response.data().userReservations().get(1);
        assertThat(secondReservation.status()).isEqualTo(ReservationStatus.COMPLETED);
        assertThat(secondReservation.enstance()).isFalse();
        assertThat(secondReservation.agentId()).isEqualTo(100L);
        assertThat(secondReservation.isReview()).isTrue();
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

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.withStatus()).thenReturn(cancelledReservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(cancelledReservation);

        SuccessResponse<NoneResponse> response = reservationService.updateStatusReservation(userId, reservationId);

        assertThat(response.successCode()).isEqualTo(RESERVATION_UPDATE_STATUS_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예약 철회 실패 - 이미 취소된 예약")
    void updateStatusReservation_Fail_AlreadyCancelled() {
        Long userId = 1L;
        Long reservationId = 1L;
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);
        Reservation reservation = mock(Reservation.class);
        when(reservation.getUser()).thenReturn(user);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CANCELLED);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.updateStatusReservation(userId, reservationId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_CANCELLED_DUPLICATED);
    }

    @Test
    @DisplayName("예약 확정 성공")
    void confirmStatusReservation_Success() {
        Long userId = 1L;
        Long reservationId = 1L;
        Long agentId = 1L;

        User user = mock(User.class);
        when(userRepository.getById(userId)).thenReturn(user);

        Agent agent = mock(Agent.class);
        when(agent.getId()).thenReturn(agentId);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);

        Room room = mock(Room.class);
        when(room.getAgent()).thenReturn(agent);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getRoom()).thenReturn(room);
        when(reservation.getStatus()).thenReturn(ReservationStatus.APPLYED);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        Reservation confirmedReservation = mock(Reservation.class);
        when(reservation.confirmStatus()).thenReturn(confirmedReservation);

        when(reservationRepository.findByRoomAndReservationTimeAndStatus(any(), any(), any()))
                .thenReturn(Optional.empty());

        SuccessResponse<NoneResponse> response = reservationService.confirmStatusReservation(userId, reservationId);

        assertThat(response.successCode()).isEqualTo(RESERVATION_CONFIRM_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);
        verify(reservationRepository).save(confirmedReservation);
    }

    @Test
    @DisplayName("예약 확정 실패 - 이미 확정된 예약")
    void confirmStatusReservation_Fail_AlreadyConfirmed() {
        Long userId = 1L;
        Long reservationId = 1L;
        Long agentId = 1L;

        User user = mock(User.class);
        when(userRepository.getById(userId)).thenReturn(user);

        Agent agent = mock(Agent.class);
        when(agent.getId()).thenReturn(agentId);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);

        Room room = mock(Room.class);
        when(room.getAgent()).thenReturn(agent);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getRoom()).thenReturn(room);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.confirmStatusReservation(userId, reservationId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_CONFIRMED_DUPLICATED);
    }
    @Test
    @DisplayName("예약 확정 실패 - 같은 시간, 같은 방에 이미 확정된 예약 존재")
    void confirmStatusReservation_Fail_DuplicateTimeAndRoom() {
        // Given
        Long userId = 1L;
        Long reservationId = 1L;
        Long agentId = 1L;

        User user = mock(User.class);
        when(userRepository.getById(userId)).thenReturn(user);

        Agent agent = mock(Agent.class);
        when(agent.getId()).thenReturn(agentId);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);

        Room room = mock(Room.class);
        when(room.getAgent()).thenReturn(agent);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getRoom()).thenReturn(room);
        when(reservation.getStatus()).thenReturn(ReservationStatus.APPLYED);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        when(reservationRepository.findByRoomAndReservationTimeAndStatus(any(), any(), any()))
                .thenReturn(Optional.of(mock(Reservation.class)));

        // When & Then
        assertThatThrownBy(() -> reservationService.confirmStatusReservation(userId, reservationId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_CONFIRMED_DUPLICATED_TIME_ROOM);
    }

    @Test
    @DisplayName("중개인 예약 목록 조회 성공 - 시간순, ID순 정렬 및 상태 변경 확인")
    void getAgentReservations_Success() {
        Long agentId = 1L;
        Long userId = 1L;
        LocalDateTime baseTime = LocalDateTime.of(2023, 1, 1, 0, 0);

        Agent agent = mock(Agent.class);
        when(agent.getId()).thenReturn(agentId);

        when(agentRepository.getByUserId(userId)).thenReturn(agent);

        Room room = mock(Room.class);
        when(room.getAgent()).thenReturn(agent);

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

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
        when(reservation1.getRoom()).thenReturn(room);
        when(reservation3.getRoom()).thenReturn(room);
        when(reservation1.getUser()).thenReturn(user);
        when(reservation3.getUser()).thenReturn(user);

        List<Reservation> reservationList = Arrays.asList(reservation1, reservation2, reservation3);
        when(reservationRepository.findByRoomAgentIdAndReservationTimeAfterOrderByReservationTimeAscIdAsc(eq(agentId), any(LocalDateTime.class)))
                .thenReturn(reservationList);

        Video video = mock(Video.class);
        when(video.getVideoStatus()).thenReturn(VideoStatus.RECORDED);
        when(videoRepository.findByReservationId(2L)).thenReturn(Optional.of(video));
        when(videoRepository.findByReservationId(3L)).thenReturn(Optional.empty());

        Reservation completedReservation2 = mock(Reservation.class);
        when(completedReservation2.getId()).thenReturn(2L);
        when(completedReservation2.getReservationTime()).thenReturn(baseTime);
        when(completedReservation2.getStatus()).thenReturn(ReservationStatus.COMPLETED);
        when(completedReservation2.getRoom()).thenReturn(room);
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
        LocalDate date = LocalDate.of(2024, 8, 9);
        AvailableTimeRequest request = new AvailableTimeRequest(roomId, date);

        Room room = mock(Room.class);
        Agent agent = mock(Agent.class);
        when(room.getAgent()).thenReturn(agent);

        AgentWorkhour workhour = mock(AgentWorkhour.class);
        when(workhour.getWeekdayStartTime()).thenReturn("09:00");
        when(workhour.getWeekdayEndTime()).thenReturn("17:30");

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(agentWorkhourRepository.findByAgent(any(Agent.class)))
                .thenReturn(Optional.of(workhour));
        when(reservationRepository.findConfirmedReservationTimes(eq(roomId), eq(date)))
                .thenReturn(List.of(LocalTime.of(10, 0), LocalTime.of(14, 30)));

        // When
        SuccessResponse<AvailableTimeResponse> response = reservationService.getAvailableTimes(request);

        // Then
        assertThat(response.successCode()).isEqualTo(AVAILABLE_TIMES_RETRIEVED);
        assertThat(response.data().availableTimes()).hasSize(15); // 9:00부터 17:30까지, 10:00와 14:30 제외
        assertThat(response.data().availableTimes()).contains("09:00", "09:30", "10:30", "11:00", "11:30","12:00", "12:30" , "13:00", "13:30", "14:00", "15:30" , "15:00", "16:00", "16:30", "17:00");

        assertThat(response.data().availableTimes()).doesNotContain("10:00", "14:30");
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
    @Test
    @DisplayName("중개인 예약 철회 성공")
    void deleteStatusReservation_Success() {
        // Given
        Long userId = 1L;
        Long reservationId = 1L;
        Long agentId = 1L;

        User user = mock(User.class);
        when(userRepository.getById(userId)).thenReturn(user);

        Agent agent = mock(Agent.class);
        when(agent.getId()).thenReturn(agentId);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);

        Room room = mock(Room.class);
        when(room.getAgent()).thenReturn(agent);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getRoom()).thenReturn(room);
        when(reservation.getStatus()).thenReturn(ReservationStatus.APPLYED);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        Reservation updatedReservation = mock(Reservation.class);
        when(reservation.withStatus()).thenReturn(updatedReservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);

        // When
        SuccessResponse<NoneResponse> response = reservationService.deleteStatusReservation(userId, reservationId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.successCode()).isEqualTo(RESERVATION_UPDATE_STATUS_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);

        verify(reservationRepository, times(1)).findById(reservationId);
        verify(userRepository, times(1)).getById(userId);
        verify(agentRepository, times(1)).getByUserId(userId);
        verify(reservation, times(1)).withStatus();
        verify(reservationRepository, times(1)).save(updatedReservation);
    }

    @Test
    @DisplayName("중개인 예약 철회 실패 - 접근 권한 없음")
    void deleteStatusReservation_Fail_AccessDenied() {
        // Given
        Long userId = 1L;
        Long reservationId = 1L;
        Long agentId = 1L;
        Long differentAgentId = 2L;

        User user = mock(User.class);
        when(userRepository.getById(userId)).thenReturn(user);

        Agent agent = mock(Agent.class);
        when(agent.getId()).thenReturn(agentId);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);

        Agent differentAgent = mock(Agent.class);
        when(differentAgent.getId()).thenReturn(differentAgentId);

        Room room = mock(Room.class);
        when(room.getAgent()).thenReturn(differentAgent);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getRoom()).thenReturn(room);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // When & Then
        assertThatThrownBy(() -> reservationService.deleteStatusReservation(userId, reservationId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ACCESS_DENIED);
    }

    @Test
    @DisplayName("중개인 예약 철회 실패 - 이미 취소된 예약")
    void deleteStatusReservation_Fail_AlreadyCancelled() {
        // Given
        Long userId = 1L;
        Long reservationId = 1L;
        Long agentId = 1L;

        User user = mock(User.class);
        when(userRepository.getById(userId)).thenReturn(user);

        Agent agent = mock(Agent.class);
        when(agent.getId()).thenReturn(agentId);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);

        Room room = mock(Room.class);
        when(room.getAgent()).thenReturn(agent);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getRoom()).thenReturn(room);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CANCELLED);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // When & Then
        assertThatThrownBy(() -> reservationService.deleteStatusReservation(userId, reservationId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_CANCELLED_DUPLICATED);
    }

    @Test
    @DisplayName("중개인 예약 철회 실패 - 이미 확정된 예약")
    void deleteStatusReservation_Fail_AlreadyConfirmed() {
        // Given
        Long userId = 1L;
        Long reservationId = 1L;
        Long agentId = 1L;

        User user = mock(User.class);
        when(userRepository.getById(userId)).thenReturn(user);

        Agent agent = mock(Agent.class);
        when(agent.getId()).thenReturn(agentId);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);

        Room room = mock(Room.class);
        when(room.getAgent()).thenReturn(agent);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getRoom()).thenReturn(room);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // When & Then
        assertThatThrownBy(() -> reservationService.deleteStatusReservation(userId, reservationId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_CANCELLED_UNAVAILABLE);
    }

}
