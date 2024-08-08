package com.bangguddle.ownbang.domain.reservation.controller;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationResponse;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.service.ReservationService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDateTime;
import java.util.List;

import static com.bangguddle.ownbang.global.enums.ErrorCode.RESERVATION_CONFIRMED_DUPLICATED_TIME_ROOM;
import static com.bangguddle.ownbang.global.enums.SuccessCode.RESERVATION_LIST_EMPTY;
import static com.bangguddle.ownbang.global.enums.SuccessCode.RESERVATION_LIST_SUCCESS;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AgentReservationController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
                {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OncePerRequestFilter.class})})
public class AgentReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("예약 확정 성공")
    @WithMockUser// userId를 1로 설정
    void confirmStatusReservation_Success() throws Exception {
        Long id = 1L;
        Long userId = 1L;

        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(
                SuccessCode.RESERVATION_CONFIRM_SUCCESS,
                NoneResponse.NONE
        );

        when(reservationService.confirmStatusReservation(any(), anyLong())).thenReturn(successResponse);

        mockMvc.perform(patch("/agents/reservations/{id}", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(SuccessCode.RESERVATION_CONFIRM_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value(SuccessCode.RESERVATION_CONFIRM_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data").value("NONE"));
    }

    @Test
    @DisplayName("예약 확정 실패 - 같은 시간, 같은 매물에 이미 확정된 예약 존재")
    @WithMockUser
    void confirmReservation_Fail_AlreadyConfirmedSameTimeAndRoom() throws Exception {
        Long id = 1L;
        Long userId = 1L;

        when(reservationService.confirmStatusReservation(any(), anyLong()))
                .thenThrow(new AppException(RESERVATION_CONFIRMED_DUPLICATED_TIME_ROOM));

        mockMvc.perform(patch("/agents/reservations/{id}", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.code").value(RESERVATION_CONFIRMED_DUPLICATED_TIME_ROOM.name()))
                .andExpect(jsonPath("$.message").value(RESERVATION_CONFIRMED_DUPLICATED_TIME_ROOM.getMessage()));
    }
    @Test
    @DisplayName("중개인 예약 목록 조회 성공")
    @WithMockUser(username = "1") // userId를 1로 설정
    void getAgentReservations_Success() throws Exception {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        String officeName = "공인중개사";
        ReservationResponse reservation1 = new ReservationResponse(1L, officeName, now, ReservationStatus.APPLYED, 1L, 1L, false);
        ReservationResponse reservation2 = new ReservationResponse(2L, officeName, now.plusDays(1), ReservationStatus.CONFIRMED, 2L, 2L, true);
        ReservationResponse reservation3 = new ReservationResponse(3L, officeName, now.plusDays(2), ReservationStatus.COMPLETED, 3L, 3L, false);

        ReservationListResponse listResponse = new ReservationListResponse(List.of(reservation1, reservation2, reservation3));
        SuccessResponse<ReservationListResponse> successResponse = new SuccessResponse<>(RESERVATION_LIST_SUCCESS, listResponse);

        when(reservationService.getAgentReservations(any())).thenReturn(successResponse);

        mockMvc.perform(get("/agents/reservations")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(RESERVATION_LIST_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value(RESERVATION_LIST_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.reservations").isArray())
                .andExpect(jsonPath("$.data.reservations.length()").value(3))
                .andExpect(jsonPath("$.data.reservations[0].id").value(1))
                .andExpect(jsonPath("$.data.reservations[0].status").value(ReservationStatus.APPLYED.toString()))
                .andExpect(jsonPath("$.data.reservations[0].enstance").value(false))
                .andExpect(jsonPath("$.data.reservations[1].id").value(2))
                .andExpect(jsonPath("$.data.reservations[1].status").value(ReservationStatus.CONFIRMED.toString()))
                .andExpect(jsonPath("$.data.reservations[1].enstance").value(true))
                .andExpect(jsonPath("$.data.reservations[2].id").value(3))
                .andExpect(jsonPath("$.data.reservations[2].status").value(ReservationStatus.COMPLETED.toString()))
                .andExpect(jsonPath("$.data.reservations[2].enstance").value(false));
    }
    @Test
    @DisplayName("중개인 예약 목록 조회 - 빈 목록")
    @WithMockUser(username = "1") // userId를 1로 설정
    void getAgentReservations_Empty() throws Exception {
        Long userId = 1L;

        ReservationListResponse emptyListResponse = new ReservationListResponse(List.of());
        SuccessResponse<ReservationListResponse> successResponse = new SuccessResponse<>(RESERVATION_LIST_EMPTY, emptyListResponse);

        when(reservationService.getAgentReservations(any())).thenReturn(successResponse);

        mockMvc.perform(get("/agents/reservations")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(RESERVATION_LIST_EMPTY.name()))
                .andExpect(jsonPath("$.message").value(RESERVATION_LIST_EMPTY.getMessage()))
                .andExpect(jsonPath("$.data.reservations").isArray())
                .andExpect(jsonPath("$.data.reservations").isEmpty());
    }

}
