package com.bangguddle.ownbang.domain.reservation.controller;

import com.bangguddle.ownbang.domain.reservation.dto.AvailableTimeRequest;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationRequest;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationResponse;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.service.ReservationService;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.repository.RoomRepository;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.SuccessCode.RESERVATION_LIST_SUCCESS;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReservationController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
                {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OncePerRequestFilter.class})})
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private RoomRepository roomRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("예약 신청 Post 컨트롤러 성공")
    @WithMockUser
    void createReservation_Success() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(
                2L,
                1L,
                now,
                ReservationStatus.APPLYED
        );

        Room room = mock(Room.class);
        User user = mock(User.class);

        when(roomRepository.findById(request.roomId())).thenReturn(Optional.of(room));
        when(userRepository.findById(request.userId())).thenReturn(Optional.of(user));

        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(
                SuccessCode.RESERVATION_MAKE_SUCCESS,
                NoneResponse.NONE
        );

        when(reservationService.createReservation(any(ReservationRequest.class))).thenReturn(successResponse);

        mockMvc.perform(post("/reservations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(SuccessCode.RESERVATION_MAKE_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value(SuccessCode.RESERVATION_MAKE_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data").value("NONE"));
    }

    @Test
    @DisplayName("Invalid Field 검증 - 조건 불만족")
    @WithMockUser
    void createReservation_Fail_InvalidField() throws Exception {
        ReservationRequest invalidRequest = ReservationRequest.of(-1L, -1L, LocalDateTime.now(), ReservationStatus.APPLYED);
        String requestBody = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/reservations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value("ERROR"));
    }

    @Test
    @DisplayName("사용자 예약 목록 조회 성공")
    @WithMockUser
    void getReservationsByUserId_Success() throws Exception {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();

        ReservationResponse reservation1 = new ReservationResponse(1L, now, ReservationStatus.APPLYED, 1L, userId);
        ReservationResponse reservation2 = new ReservationResponse(2L, now.plusDays(1), ReservationStatus.CONFIRMED, 2L, userId);

        List<ReservationResponse> reservations = List.of(reservation1, reservation2);
        ReservationListResponse listResponse = new ReservationListResponse(reservations);
        SuccessResponse<ReservationListResponse> successResponse = new SuccessResponse<>(RESERVATION_LIST_SUCCESS, listResponse);

        when(reservationService.getMyReservationList(eq(userId))).thenReturn(successResponse);

        mockMvc.perform(get("/reservations/list")
                        .param("userId", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(RESERVATION_LIST_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value(RESERVATION_LIST_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.reservations").isArray())
                .andExpect(jsonPath("$.data.reservations", hasSize(2)))
                .andExpect(jsonPath("$.data.reservations[0].id").value(1))
                .andExpect(jsonPath("$.data.reservations[0].reservationTime").exists())
                .andExpect(jsonPath("$.data.reservations[0].status").value(ReservationStatus.APPLYED.toString()))
                .andExpect(jsonPath("$.data.reservations[0].roomId").value(1))
                .andExpect(jsonPath("$.data.reservations[0].userId").value(userId))
                .andExpect(jsonPath("$.data.reservations[1].id").value(2))
                .andExpect(jsonPath("$.data.reservations[1].reservationTime").exists())
                .andExpect(jsonPath("$.data.reservations[1].status").value(ReservationStatus.CONFIRMED.toString()))
                .andExpect(jsonPath("$.data.reservations[1].roomId").value(2))
                .andExpect(jsonPath("$.data.reservations[1].userId").value(userId));

    }
    @Test
    @DisplayName("예약 목록 조회 시 빈 리스트 반환")
    @WithMockUser
    void getReservationsByUserId_EmptyList() throws Exception {
        long userId = 1L;
        ReservationListResponse listResponse = new ReservationListResponse(List.of());
        SuccessResponse<ReservationListResponse> successResponse = new SuccessResponse<>(SuccessCode.RESERVATION_LIST_EMPTY, listResponse);

        when(reservationService.getMyReservationList(anyLong())).thenReturn(successResponse);

        mockMvc.perform(get("/reservations/list")
                        .param("userId", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(SuccessCode.RESERVATION_LIST_EMPTY.name()))
                .andExpect(jsonPath("$.message").value(SuccessCode.RESERVATION_LIST_EMPTY.getMessage()))
                .andExpect(jsonPath("$.data.reservations").isArray())
                .andExpect(jsonPath("$.data.reservations").isEmpty());
    }

    @Test
    @DisplayName("예약 철회 성공")
    @WithMockUser
    void updateStatusReservation_Success() throws Exception {
        Long id = 1L;

        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(
                SuccessCode.RESERVATION_UPDATE_STATUS_SUCCESS,
                NoneResponse.NONE
        );

        when(reservationService.updateStatusReservation(anyLong())).thenReturn(successResponse);

        mockMvc.perform(patch("/reservations/{id}", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(SuccessCode.RESERVATION_UPDATE_STATUS_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value(SuccessCode.RESERVATION_UPDATE_STATUS_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data").value("NONE"));
    }

    @Test
    @DisplayName("사용자 예약 목록 조회 성공 - COMPLETED 상태 포함")
    @WithMockUser
    void getReservationsByUserId_Success_WithCompletedStatus() throws Exception {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();

        ReservationResponse reservation1 = new ReservationResponse(1L, now, ReservationStatus.APPLYED, 1L, userId);
        ReservationResponse reservation2 = new ReservationResponse(2L, now.plusDays(1), ReservationStatus.CONFIRMED, 2L, userId);
        ReservationResponse reservation3 = new ReservationResponse(3L, now.plusDays(2), ReservationStatus.COMPLETED, 3L, userId);

        List<ReservationResponse> reservations = List.of(reservation1, reservation2, reservation3);
        ReservationListResponse listResponse = new ReservationListResponse(reservations);
        SuccessResponse<ReservationListResponse> successResponse = new SuccessResponse<>(RESERVATION_LIST_SUCCESS, listResponse);

        when(reservationService.getMyReservationList(eq(userId))).thenReturn(successResponse);

        mockMvc.perform(get("/reservations/list")
                        .param("userId", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(RESERVATION_LIST_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value(RESERVATION_LIST_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.reservations").isArray())
                .andExpect(jsonPath("$.data.reservations", hasSize(3)))
                .andExpect(jsonPath("$.data.reservations[0].id").value(1))
                .andExpect(jsonPath("$.data.reservations[0].status").value(ReservationStatus.APPLYED.toString()))
                .andExpect(jsonPath("$.data.reservations[1].id").value(2))
                .andExpect(jsonPath("$.data.reservations[1].status").value(ReservationStatus.CONFIRMED.toString()))
                .andExpect(jsonPath("$.data.reservations[2].id").value(3))
                .andExpect(jsonPath("$.data.reservations[2].status").value(ReservationStatus.COMPLETED.toString()));
    }

    @Test
    @DisplayName("잘못된 요청으로 예약 가능 시간 조회 실패")
    void getAvailableTimes_BadRequest() throws Exception {
        // Given
        Long roomId = 1L;
        String invalidDate = "invalid-date";

        // When & Then
        mockMvc.perform(get("/reservations/available-times")
                        .param("roomId", String.valueOf(roomId))
                        .param("date", invalidDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("서비스 예외 발생 시 실패")
    void getAvailableTimes_ServiceException() throws Exception {
        // Given
        Long roomId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);

        when(reservationService.getAvailableTimes(any(AvailableTimeRequest.class)))
                .thenThrow(new AppException(ErrorCode.ROOM_NOT_FOUND));

        // When & Then
        mockMvc.perform(get("/reservations/available-times")
                        .param("roomId", String.valueOf(roomId))
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.code").value(ErrorCode.ROOM_NOT_FOUND.name()));
    }
}
