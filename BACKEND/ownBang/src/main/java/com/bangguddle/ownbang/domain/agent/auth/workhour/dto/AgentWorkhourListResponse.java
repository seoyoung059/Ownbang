package com.bangguddle.ownbang.domain.agent.auth.workhour.dto;

import com.bangguddle.ownbang.domain.agent.entity.AgentWorkhour;

import java.util.List;

public record AgentWorkhourListResponse(
        List<AgentWorkhourResponse> agentWorkhours
) {
    public static AgentWorkhourListResponse from(List<AgentWorkhour> agentWorkhours) {
        List<AgentWorkhourResponse> responses = agentWorkhours.stream()
                .map(AgentWorkhourResponse::from)
                .toList();
        return new AgentWorkhourListResponse(responses);
    }
}