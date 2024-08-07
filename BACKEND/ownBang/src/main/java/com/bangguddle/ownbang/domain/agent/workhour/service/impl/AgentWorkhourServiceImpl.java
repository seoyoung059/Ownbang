package com.bangguddle.ownbang.domain.agent.workhour.service.impl;

import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourRequest;
import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourResponse;
import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;
import com.bangguddle.ownbang.domain.agent.repository.AgentRepository;
import com.bangguddle.ownbang.domain.agent.workhour.repository.AgentWorkhourRepository;
import com.bangguddle.ownbang.domain.agent.workhour.service.AgentWorkhourService;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.ACCESS_DENIED;
import static com.bangguddle.ownbang.global.enums.ErrorCode.WORKHOUR_NOT_FOUND;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;

@Service
@RequiredArgsConstructor
public class AgentWorkhourServiceImpl implements AgentWorkhourService {

    private final AgentWorkhourRepository agentWorkhourRepository;
    private final AgentRepository agentRepository;
    private final UserRepository userRepository;
    @Override
    public SuccessResponse<NoneResponse> createAgentWorkhour(Long userId, AgentWorkhourRequest request) {
        User user = userRepository.getById(userId);
        Agent agent = agentRepository.getByUserId(userId);

        AgentWorkhour agentWorkhour = request.toEntity(agent);
        agentWorkhourRepository.save(agentWorkhour);

        return new SuccessResponse<>(AGENT_WORKHOUR_CREATE_SUCCESS, NoneResponse.NONE);
    }

    @Override
    @Transactional(readOnly = true)
    public SuccessResponse<AgentWorkhourResponse> getAgentWorkhour(Long agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new AppException(WORKHOUR_NOT_FOUND));
        AgentWorkhour agentWorkhour = agentWorkhourRepository.findByAgent(agent)
                .orElseThrow(() -> new AppException(WORKHOUR_NOT_FOUND));
        return new SuccessResponse<>(AGENT_WORKHOUR_GET_SUCCESS, AgentWorkhourResponse.from(agentWorkhour));
    }

    /*
    중개인 업무시간 수정
    id는 agentworkhourid
    */
    @Transactional
    public SuccessResponse<NoneResponse> updateAgentWorkhour( Long userId, AgentWorkhourRequest request) {
        User user = userRepository.getById(userId);
        Agent agent = agentRepository.getByUserId(userId);
        Long agentId = agent.getId();
        System.out.println(agentId);

        AgentWorkhour agentWorkhour = agentWorkhourRepository.findByAgent(agent)
                .orElseThrow(() -> new AppException(WORKHOUR_NOT_FOUND));
        
        agentWorkhour.updateWorkhour(request.weekdayStartTime(), request.weekdayEndTime(), request.weekendStartTime(), request.weekendEndTime());
        agentWorkhourRepository.save(agentWorkhour);

        return new SuccessResponse<>(AGENT_WORKHOUR_UPDATE_SUCCESS, NoneResponse.NONE);
    }
}