package com.bangguddle.ownbang.domain.agent.workhour.service;

import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourListResponse;
import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourRequest;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface AgentWorkhourService {
    SuccessResponse<NoneResponse> createAgentWorkhour(Long userId, AgentWorkhourRequest request);
    SuccessResponse<AgentWorkhourListResponse> getAgentWorkhour(Long userId);
    SuccessResponse<NoneResponse> updateAgentWorkhour(Long id, Long userId, AgentWorkhourRequest request);
}
