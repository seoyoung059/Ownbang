package com.bangguddle.ownbang.domain.reservation.controller;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationRequest;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.service.ReservationService;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void createReservation_Success() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(
                2L,
                1L,
                now,
                ReservationStatus.예약신청
        );

        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(
                SuccessCode.RESERVATION_MAKE_SUCCESS,
                NoneResponse.NONE
        );

        when(reservationService.createReservation(any(ReservationRequest.class))).thenReturn(successResponse);

        mockMvc.perform(post("/api/reservations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(SuccessCode.RESERVATION_MAKE_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value(SuccessCode.RESERVATION_MAKE_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data").value("NONE"));
    }

    @Test
    @WithMockUser
    void createReservation_Fail_MissingField() throws Exception {
        String invalidRequest = "{}"; // 전체 요청이 누락된 경우

        mockMvc.perform(post("/api/reservations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.code").value(ErrorCode.INTERNAL_SERVER_ERROR.name()))
                .andExpect(jsonPath("$.message").value("서버 내부적 에러가 발생했습니다."))
                .andExpect(jsonPath("$.data").value("NONE"));
    }


    @Test
    @WithMockUser
    void createReservation_Fail_InvalidField() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        ReservationRequest request = new ReservationRequest(
                -1L,  // 잘못된 roomId
                -1L,  // 잘못된 userId
                now,
                ReservationStatus.예약신청
        );

        String invalidRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/reservations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_ID.name()))
                .andExpect(jsonPath("$.message").value("유효하지 않은 ID가 제공되었습니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser
    void getReservationsByUserId_Success() throws Exception {
        long userId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Reservation reservation1 = Reservation.builder()
                .roomId(101L)
                .userId(userId)
                .time(now)
                .status(ReservationStatus.예약신청)
                .build();

        Reservation reservation2 = Reservation.builder()
                .roomId(102L)
                .userId(userId)
                .time(now.plusDays(1))
                .status(ReservationStatus.예약확정)
                .build();

        ReservationListResponse listResponse = new ReservationListResponse(Arrays.asList(reservation1, reservation2));
        SuccessResponse<ReservationListResponse> successResponse = new SuccessResponse<>(SuccessCode.RESERVATION_LIST_SUCCESS, listResponse);

        when(reservationService.getReservationsByUserId(anyLong())).thenReturn(successResponse);

        MvcResult result = mockMvc.perform(get("/api/reservations/list")
                        .param("userId", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(SuccessCode.RESERVATION_LIST_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value(SuccessCode.RESERVATION_LIST_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.reservations").isArray())
                .andExpect(jsonPath("$.data.reservations.length()").value(2))
                .andExpect(jsonPath("$.data.reservations[0].roomId").value(101))
                .andExpect(jsonPath("$.data.reservations[0].status").value(ReservationStatus.예약신청.toString()))
                .andExpect(jsonPath("$.data.reservations[1].roomId").value(102))
                .andExpect(jsonPath("$.data.reservations[1].status").value(ReservationStatus.예약확정.toString()))
                .andReturn();

        System.out.println("Response Body: " + result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser
    void getReservationsByUserId_EmptyList() throws Exception {
        long userId = 1L;
        ReservationListResponse listResponse = new ReservationListResponse(List.of());
        SuccessResponse<ReservationListResponse> successResponse = new SuccessResponse<>(SuccessCode.RESERVATION_LIST_EMPTY, listResponse);

        when(reservationService.getReservationsByUserId(anyLong())).thenReturn(successResponse);

        mockMvc.perform(get("/api/reservations/list")
                        .param("userId", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(SuccessCode.RESERVATION_LIST_EMPTY.name()))
                .andExpect(jsonPath("$.message").value(SuccessCode.RESERVATION_LIST_EMPTY.getMessage()))
                .andExpect(jsonPath("$.data.reservations").isArray())
                .andExpect(jsonPath("$.data.reservations").isEmpty());
    }
}
