package com.bangguddle.ownbang.domain.agent.auth.service;

import com.bangguddle.ownbang.domain.agent.auth.dto.AgentWorkhourListResponse;
import com.bangguddle.ownbang.domain.agent.entity.AgentWorkhour;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface AgentWorkhourService {
    SuccessResponse<NoneResponse> createAgentWorkhour(AgentWorkhour agentWorkhour);
    SuccessResponse<AgentWorkhourListResponse> getAgentWorkhour(Long agentId);
    SuccessResponse<NoneResponse> updateAgentWorkhour(Long id, AgentWorkhour agentWorkhour);
}
