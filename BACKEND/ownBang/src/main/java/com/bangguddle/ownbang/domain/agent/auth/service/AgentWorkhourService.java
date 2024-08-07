package com.bangguddle.ownbang.domain.agent.auth.service;

import com.bangguddle.ownbang.domain.agent.auth.dto.AgentWorkhourListResponse;
import com.bangguddle.ownbang.domain.agent.auth.dto.AgentWorkhourRequest;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface AgentWorkhourService {
    SuccessResponse<NoneResponse> createAgentWorkhour(AgentWorkhourRequest request);
    SuccessResponse<AgentWorkhourListResponse> getAgentWorkhour(Long agentId);
    SuccessResponse<NoneResponse> updateAgentWorkhour(Long id, AgentWorkhourRequest request);
}
