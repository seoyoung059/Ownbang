package com.bangguddle.ownbang.domain.workhour.controller;
import com.bangguddle.ownbang.domain.agent.workhour.controller.AgentWorkhourController;
import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourRequest;
import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourResponse;
import com.bangguddle.ownbang.domain.agent.workhour.service.AgentWorkhourService;
import com.bangguddle.ownbang.domain.reservation.controller.AgentReservationController;
import com.bangguddle.ownbang.global.enums.NoneResponse;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AgentWorkhourController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
                {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OncePerRequestFilter.class})})

class AgentWorkhourControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgentWorkhourService agentWorkhourService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("근무 시간 생성 성공")
    @WithMockUser()
    void createAgentWorkhour_Success() throws Exception {
        AgentWorkhourRequest request = new AgentWorkhourRequest("09:00", "18:00", "10:00", "17:00");
        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(AGENT_WORKHOUR_CREATE_SUCCESS, NoneResponse.NONE);

        when(agentWorkhourService.createAgentWorkhour(any(), any(AgentWorkhourRequest.class))).thenReturn(successResponse);

        mockMvc.perform(post("/agents/workhour")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(AGENT_WORKHOUR_CREATE_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value(AGENT_WORKHOUR_CREATE_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data").value("NONE"));
    }

    @Test
    @DisplayName("근무 시간 조회 성공")
    @WithMockUser
    void getAgentWorkhour_Success() throws Exception {
        Long id =1L; // workhour id
        Long agentId = 1L;
        AgentWorkhourResponse workhourResponse = new AgentWorkhourResponse(any(),agentId,"09:00", "18:00", "10:00", "17:00");
        SuccessResponse<AgentWorkhourResponse> successResponse = new SuccessResponse<>(AGENT_WORKHOUR_GET_SUCCESS, workhourResponse);

        when(agentWorkhourService.getAgentWorkhour(agentId)).thenReturn(successResponse);

        mockMvc.perform(get("/agents/workhour")
                        .param("agentId", agentId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(AGENT_WORKHOUR_GET_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value(AGENT_WORKHOUR_GET_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.weekdayStartTime").value("09:00"))
                .andExpect(jsonPath("$.data.weekdayEndTime").value("18:00"))
                .andExpect(jsonPath("$.data.weekendStartTime").value("10:00"))
                .andExpect(jsonPath("$.data.weekendEndTime").value("17:00"));
    }

    @Test
    @DisplayName("근무 시간 수정 성공")
    @WithMockUser()
    void updateAgentWorkhour_Success() throws Exception {
        AgentWorkhourRequest request = new AgentWorkhourRequest("08:00", "19:00", "11:00", "16:00");
        SuccessResponse<NoneResponse> successResponse = new SuccessResponse<>(AGENT_WORKHOUR_UPDATE_SUCCESS, NoneResponse.NONE);

        when(agentWorkhourService.updateAgentWorkhour(any(), any(AgentWorkhourRequest.class))).thenReturn(successResponse);

        mockMvc.perform(patch("/agents/workhour")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(AGENT_WORKHOUR_UPDATE_SUCCESS.name()))
                .andExpect(jsonPath("$.message").value(AGENT_WORKHOUR_UPDATE_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data").value("NONE"));
    }
}