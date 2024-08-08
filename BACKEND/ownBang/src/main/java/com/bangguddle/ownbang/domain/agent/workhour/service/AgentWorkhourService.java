package com.bangguddle.ownbang.domain.agent.workhour.service;

import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourRequest;
import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourResponse;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface AgentWorkhourService {
    SuccessResponse<NoneResponse> createAgentWorkhour(Long userId, AgentWorkhourRequest request);
    SuccessResponse<AgentWorkhourResponse> getAgentWorkhour(Long agentId);
    SuccessResponse<NoneResponse> updateAgentWorkhour(Long userId, AgentWorkhourRequest request);
}
