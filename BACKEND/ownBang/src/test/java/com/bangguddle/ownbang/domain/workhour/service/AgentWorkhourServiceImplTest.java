package com.bangguddle.ownbang.domain.workhour.service;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.repository.AgentRepository;
import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourRequest;
import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourResponse;
import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;
import com.bangguddle.ownbang.domain.agent.workhour.repository.AgentWorkhourRepository;
import com.bangguddle.ownbang.domain.agent.workhour.service.impl.AgentWorkhourServiceImpl;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.WORKHOUR_INAVAILABLE;
import static com.bangguddle.ownbang.global.enums.ErrorCode.WORKHOUR_NOT_FOUND;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentWorkhourServiceImplTest {

    @InjectMocks
    private AgentWorkhourServiceImpl agentWorkhourService;

    @Mock
    private AgentWorkhourRepository agentWorkhourRepository;
    @Mock
    private AgentRepository agentRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("근무 시간 생성 성공")
    void createAgentWorkhour_Success() {
        // Given
        Long userId = 1L;
        AgentWorkhourRequest request = new AgentWorkhourRequest("09:00", "18:00", "10:00", "17:00");
        User user = mock(User.class);
        Agent agent = mock(Agent.class);
        AgentWorkhour agentWorkhour = mock(AgentWorkhour.class);

        when(userRepository.getById(userId)).thenReturn(user);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);
        when(agentWorkhourRepository.save(any(AgentWorkhour.class))).thenReturn(agentWorkhour);

        // When
        SuccessResponse<NoneResponse> response = agentWorkhourService.createAgentWorkhour(userId, request);

        // Then
        assertThat(response.successCode()).isEqualTo(AGENT_WORKHOUR_CREATE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);
        verify(agentWorkhourRepository).save(any(AgentWorkhour.class));
    }

    @Test
    @DisplayName("근무 시간 생성 실패 - 잘못된 시간 입력")
    void createAgentWorkhour_Fail_InvalidTime() {
        // Given
        Long userId = 1L;
        AgentWorkhourRequest request = new AgentWorkhourRequest("18:00", "09:00", "17:00", "10:00");
        User user = mock(User.class);
        Agent agent = mock(Agent.class);

        when(userRepository.getById(userId)).thenReturn(user);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);

        // When & Then
        assertThatThrownBy(() -> agentWorkhourService.createAgentWorkhour(userId, request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", WORKHOUR_INAVAILABLE);
    }

    @Test
    @DisplayName("근무 시간 조회 성공")
    void getAgentWorkhour_Success() {
        // Given
        Long agentId = 1L;
        Agent agent = mock(Agent.class);
        AgentWorkhour agentWorkhour = mock(AgentWorkhour.class);

        when(agentRepository.findById(agentId)).thenReturn(Optional.of(agent));
        when(agentWorkhourRepository.findByAgent(agent)).thenReturn(Optional.of(agentWorkhour));
        when(agentWorkhour.getAgent()).thenReturn(agent);
        when(agent.getId()).thenReturn(agentId);
        when(agentWorkhour.getWeekdayStartTime()).thenReturn("09:00");
        when(agentWorkhour.getWeekdayEndTime()).thenReturn("18:00");
        when(agentWorkhour.getWeekendStartTime()).thenReturn("10:00");
        when(agentWorkhour.getWeekendEndTime()).thenReturn("17:00");

        // When
        SuccessResponse<AgentWorkhourResponse> response = agentWorkhourService.getAgentWorkhour(agentId);

        // Then
        assertThat(response.successCode()).isEqualTo(AGENT_WORKHOUR_GET_SUCCESS);
        assertThat(response.data()).isNotNull();
        assertThat(response.data().weekdayStartTime()).isEqualTo("09:00");
        assertThat(response.data().weekdayEndTime()).isEqualTo("18:00");
        assertThat(response.data().weekendStartTime()).isEqualTo("10:00");
        assertThat(response.data().weekendEndTime()).isEqualTo("17:00");
    }

    @Test
    @DisplayName("근무 시간 조회 실패 - 중개인 없음")
    void getAgentWorkhour_Fail_AgentNotFound() {
        // Given
        Long agentId = 1L;
        when(agentRepository.findById(agentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> agentWorkhourService.getAgentWorkhour(agentId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", WORKHOUR_NOT_FOUND);
    }

    @Test
    @DisplayName("근무 시간 수정 성공")
    void updateAgentWorkhour_Success() {
        // Given
        Long userId = 1L;
        AgentWorkhourRequest request = new AgentWorkhourRequest("08:00", "19:00", "11:00", "16:00");
        User user = mock(User.class);
        Agent agent = mock(Agent.class);
        AgentWorkhour agentWorkhour = mock(AgentWorkhour.class);

        when(userRepository.getById(userId)).thenReturn(user);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);
        when(agentWorkhourRepository.findByAgent(agent)).thenReturn(Optional.of(agentWorkhour));
        when(agentWorkhourRepository.save(any(AgentWorkhour.class))).thenReturn(agentWorkhour);

        // When
        SuccessResponse<NoneResponse> response = agentWorkhourService.updateAgentWorkhour(userId, request);

        // Then
        assertThat(response.successCode()).isEqualTo(AGENT_WORKHOUR_UPDATE_SUCCESS);
        assertThat(response.data()).isEqualTo(NoneResponse.NONE);
        verify(agentWorkhourRepository).save(any(AgentWorkhour.class));
    }

    @Test
    @DisplayName("근무 시간 수정 실패 - 잘못된 시간 입력")
    void updateAgentWorkhour_Fail_InvalidTime() {
        // Given
        Long userId = 1L;
        AgentWorkhourRequest request = new AgentWorkhourRequest("19:00", "08:00", "16:00", "11:00");
        User user = mock(User.class);
        Agent agent = mock(Agent.class);
        AgentWorkhour agentWorkhour = mock(AgentWorkhour.class);

        when(userRepository.getById(userId)).thenReturn(user);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);
        when(agentWorkhourRepository.findByAgent(agent)).thenReturn(Optional.of(agentWorkhour));

        // When & Then
        assertThatThrownBy(() -> agentWorkhourService.updateAgentWorkhour(userId, request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", WORKHOUR_INAVAILABLE);
    }

    @Test
    @DisplayName("근무 시간 수정 실패 - 근무 시간 정보 없음")
    void updateAgentWorkhour_Fail_WorkhourNotFound() {
        // Given
        Long userId = 1L;
        AgentWorkhourRequest request = new AgentWorkhourRequest("08:00", "19:00", "11:00", "16:00");
        User user = mock(User.class);
        Agent agent = mock(Agent.class);

        when(userRepository.getById(userId)).thenReturn(user);
        when(agentRepository.getByUserId(userId)).thenReturn(agent);
        when(agentWorkhourRepository.findByAgent(agent)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> agentWorkhourService.updateAgentWorkhour(userId, request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", WORKHOUR_NOT_FOUND);
    }
}