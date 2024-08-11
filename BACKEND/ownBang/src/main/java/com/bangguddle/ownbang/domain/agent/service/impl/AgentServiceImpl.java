package com.bangguddle.ownbang.domain.agent.service.impl;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.mypage.dto.AgentMyPageModifyRequest;
import com.bangguddle.ownbang.domain.agent.mypage.dto.AgentMyPageResponse;
import com.bangguddle.ownbang.domain.agent.repository.AgentRepository;
import com.bangguddle.ownbang.domain.agent.service.AgentService;
import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;
import com.bangguddle.ownbang.domain.agent.workhour.repository.AgentWorkhourRepository;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.bangguddle.ownbang.global.enums.SuccessCode.GET_AGENT_MY_PAGE_SUCCESS;
import static com.bangguddle.ownbang.global.enums.SuccessCode.UPDATE_AGENT_MY_PAGE_SUCCESS;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {
    private final UserRepository userRepository;
    private final AgentRepository agentRepository;
    private final AgentWorkhourRepository agentWorkhourRepository;

    @Override
    public SuccessResponse<AgentMyPageResponse> getMyPage(Long id) {
        userRepository.getById(id);
        Agent agent = agentRepository.getByUserId(id);
        AgentWorkhour agentWorkhour = getByAgent(agent);
        AgentMyPageResponse response = AgentMyPageResponse.from(agent, agentWorkhour);
        return new SuccessResponse<>(GET_AGENT_MY_PAGE_SUCCESS, response);

    }

    @Override
    public SuccessResponse<NoneResponse> modifyMyPage(Long id, AgentMyPageModifyRequest request) {
        userRepository.getById(id);
        Agent agent = agentRepository.getByUserId(id);
        AgentWorkhour agentWorkhour = getByAgent(agent);
        agent.updateAgent(request);
        agentWorkhour.updateWorkhour(request.weekdayStartTime(),request.weekdayEndTime(),request.weekendStartTime(),
                request.weekendEndTime());
        agentRepository.save(agent);
        agentWorkhourRepository.save(agentWorkhour);
        return new SuccessResponse<>(UPDATE_AGENT_MY_PAGE_SUCCESS, NoneResponse.NONE);
    }

    private AgentWorkhour getByAgent(Agent agent) {
        return agentWorkhourRepository.findByAgent(agent)
                .orElseThrow(() -> new AppException(ErrorCode.AGENT_INFO_NOT_EXIST));
    }

}
