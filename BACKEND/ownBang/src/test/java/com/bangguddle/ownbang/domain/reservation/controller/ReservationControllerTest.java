package com.bangguddle.ownbang.domain.reservation.controller;

import com.bangguddle.ownbang.domain.reservation.dto.*;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.service.ReservationService;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReservationController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
                {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OncePerRequestFilter.class})})
 class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("예약 신청 성공")
    @WithMockUser()
    void createReservation_Success() throws Exception {
        Long userId = 1L;
        ReservationRequest request = new ReservationRequest(1L, LocalDateTime.now(), ReservationStatus.APPLYED);
        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(RESERVATION_MAKE_SUCCESS, NoneResponse.NONE);

        when(reservationService.createReservation(any(), any())).thenReturn(successResponse);

        mockMvc.perform(post("/reservations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(SecurityMockMvcRequestPostProcessors.user(userId.toString()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(RESERVATION_MAKE_SUCCESS.name()))
                .andExpect(jsonPath("$.data").value("NONE"))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 신청 실패 - 유효하지 않은 필드")
    @WithMockUser
    void createReservation_Fail_InvalidField() throws Exception {
        ReservationRequest invalidRequest = new ReservationRequest(-1L, LocalDateTime.now(), null);

        mockMvc.perform(post("/reservations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andDo(print());
    }

    @Test
    @DisplayName("사용자 예약 목록 조회 성공")
    @WithMockUser
    void getMyReservationList_Success() throws Exception {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        String officeName = "공인중개사 이름";

        ReservationResponse reservation1 = new ReservationResponse(1L, officeName, now, ReservationStatus.APPLYED, 1L, userId);
        ReservationResponse reservation2 = new ReservationResponse(2L, officeName, now.plusDays(1), ReservationStatus.CONFIRMED, 2L, userId);
        ReservationResponse reservation3 = new ReservationResponse(3L, officeName, now.plusDays(2), ReservationStatus.COMPLETED, 3L, userId);

        List<ReservationResponse> reservations = List.of(reservation1, reservation2, reservation3);
        ReservationListResponse listResponse = new ReservationListResponse(reservations);
        SuccessResponse<ReservationListResponse> successResponse = new SuccessResponse<>(RESERVATION_LIST_SUCCESS, listResponse);

        when(reservationService.getMyReservationList(any())).thenReturn(successResponse);

        mockMvc.perform(get("/reservations/list")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
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
    @DisplayName("예약 상태 업데이트 성공")
    @WithMockUser(username = "1")
    void updateStatusReservation_Success() throws Exception {
        Long userId = 1L;
        Long reservationId = 1L;
        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(RESERVATION_UPDATE_STATUS_SUCCESS, NoneResponse.NONE);

        when(reservationService.updateStatusReservation(any(), any())).thenReturn(successResponse);

        mockMvc.perform(patch("/reservations/{id}", reservationId)
                        .with(SecurityMockMvcRequestPostProcessors.user(userId.toString()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(RESERVATION_UPDATE_STATUS_SUCCESS.name()))
                .andExpect(jsonPath("$.data").value("NONE"))
                .andDo(print());
    }



    @Test
    @DisplayName("사용자 예약 목록 조회 성공 - COMPLETED 상태 포함")
    @WithMockUser
    void getReservationsByUserId_Success_WithCompletedStatus() throws Exception {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        String officeName = "공인중개사 이름";

        ReservationResponse reservation1 = new ReservationResponse(1L, officeName, now, ReservationStatus.APPLYED, 1L, userId);
        ReservationResponse reservation2 = new ReservationResponse(2L, officeName, now.plusDays(1), ReservationStatus.CONFIRMED, 2L, userId);
        ReservationResponse reservation3 = new ReservationResponse(3L, officeName, now.plusDays(2), ReservationStatus.COMPLETED, 3L, userId);

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
    @Test
    @DisplayName("예약 가능 시간 조회 성공")
    @WithMockUser
    void getAvailableTimes_Success() throws Exception {
        Long roomId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
        List<String> availableTimes = List.of("09:00", "10:00", "11:00");
        AvailableTimeResponse response = new AvailableTimeResponse(availableTimes);
        SuccessResponse<AvailableTimeResponse> successResponse = new SuccessResponse<>(AVAILABLE_TIMES_RETRIEVED, response);

        when(reservationService.getAvailableTimes(any(AvailableTimeRequest.class))).thenReturn(successResponse);

        mockMvc.perform(get("/reservations/available-times")
                        .param("roomId", String.valueOf(roomId))
                        .param("date", date.toString())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(AVAILABLE_TIMES_RETRIEVED.name()))
                .andExpect(jsonPath("$.data.availableTimes", hasSize(3)))
                .andDo(print());
    }

}
