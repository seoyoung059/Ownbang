package com.bangguddle.ownbang.domain.reservation.controller;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationRequest;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.service.ReservationService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(
                SuccessCode.RESERVATION_MAKE_SUCCESS,
                NoneResponse.NONE
        );

        when(reservationService.createReservation(any(ReservationRequest.class))).thenReturn(successResponse);

        mockMvc.perform(post("/api/reservations/")
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

        mockMvc.perform(post("/api/reservations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value("ERROR"));
    }

    @Test
    @DisplayName("예약 목록 조회 성공")
    @WithMockUser
    void getReservationsByUserId_Success() throws Exception {
        long userId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Reservation reservation1 = Reservation.builder()
                .roomId(101L)
                .userId(userId)
                .reservationTime(now)
                .status(ReservationStatus.APPLYED)
                .build();

        Reservation reservation2 = Reservation.builder()
                .roomId(102L)
                .userId(userId)
                .reservationTime(now.plusDays(1))
                .status(ReservationStatus.CONFIRMED)
                .build();

        ReservationListResponse listResponse = new ReservationListResponse(Arrays.asList(reservation1, reservation2));
        SuccessResponse<ReservationListResponse> successResponse = new SuccessResponse<>(SuccessCode.RESERVATION_LIST_SUCCESS, listResponse);

        when(reservationService.getMyReservationList(anyLong())).thenReturn(successResponse);

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
                .andExpect(jsonPath("$.data.reservations[0].status").value(ReservationStatus.APPLYED.toString()))
                .andExpect(jsonPath("$.data.reservations[1].roomId").value(102))
                .andExpect(jsonPath("$.data.reservations[1].status").value(ReservationStatus.CONFIRMED.toString()))
                .andReturn();

        System.out.println("Response Body: " + result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("예약 목록 조회 시 빈 리스트 반환")
    @WithMockUser
    void getReservationsByUserId_EmptyList() throws Exception {
        long userId = 1L;
        ReservationListResponse listResponse = new ReservationListResponse(List.of());
        SuccessResponse<ReservationListResponse> successResponse = new SuccessResponse<>(SuccessCode.RESERVATION_LIST_EMPTY, listResponse);

        when(reservationService.getMyReservationList(anyLong())).thenReturn(successResponse);

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

        mockMvc.perform(patch("/api/reservations/{id}", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(SuccessCode.RESERVATION_UPDATE_STATUS_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value(SuccessCode.RESERVATION_UPDATE_STATUS_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data").value("NONE"));
    }

}
