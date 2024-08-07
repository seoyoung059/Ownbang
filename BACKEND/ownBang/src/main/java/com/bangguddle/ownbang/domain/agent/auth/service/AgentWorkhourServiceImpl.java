package com.bangguddle.ownbang.domain.agent.auth.service;

import com.bangguddle.ownbang.domain.agent.auth.dto.AgentWorkhourListResponse;
import com.bangguddle.ownbang.domain.agent.auth.dto.AgentWorkhourRequest;
import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.entity.AgentWorkhour;
import com.bangguddle.ownbang.domain.agent.repository.AgentRepository;
import com.bangguddle.ownbang.domain.agent.repository.AgentWorkhourRepository;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bangguddle.ownbang.global.enums.ErrorCode.NOT_FOUND;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;

@Service
@RequiredArgsConstructor
public class AgentWorkhourServiceImpl implements AgentWorkhourService {

    private final AgentWorkhourRepository agentWorkhourRepository;
    private final AgentRepository agentRepository;

    @Override
    public SuccessResponse<NoneResponse> createAgentWorkhour(AgentWorkhourRequest request) {
        Agent agent = agentRepository.findById(request.agentId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        AgentWorkhour agentWorkhour = request.toEntity(agent);
        agentWorkhourRepository.save(agentWorkhour);

        return new SuccessResponse<>(AGENT_WORKHOUR_CREATE_SUCCESS, NoneResponse.NONE);
    }

    @Override
    @Transactional(readOnly = true)
    public SuccessResponse<AgentWorkhourListResponse> getAgentWorkhour(Long agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new AppException(NOT_FOUND));
        List<AgentWorkhour> workhours = agentWorkhourRepository.findByAgent(agent);

        return new SuccessResponse<>(AGENT_WORKHOUR_GET_SUCCESS, AgentWorkhourListResponse.from(workhours));
    }

    @Transactional
    public SuccessResponse<NoneResponse> updateAgentWorkhour(Long id, AgentWorkhourRequest request) {
        AgentWorkhour agentWorkhour = agentWorkhourRepository.findById(id)
                .orElseThrow(() -> new AppException(NOT_FOUND));

        // 요청된 agentId와 실제 AgentWorkhour의 agentId가 일치하는지 확인
        if (!agentWorkhour.getAgent().getId().equals(request.agentId())) {
            throw new AppException(NOT_FOUND);
        }

        agentWorkhour.updateWorkhour(id,request.startTime(), request.endTime());


        return new SuccessResponse<>(AGENT_WORKHOUR_UPDATE_SUCCESS, NoneResponse.NONE);
    }
}