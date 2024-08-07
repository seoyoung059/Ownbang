package com.bangguddle.ownbang.domain.agent.auth.workhour.dto;

import com.bangguddle.ownbang.domain.agent.entity.AgentWorkhour;

public record AgentWorkhourResponse(
        Long id,
        Long agentId,
        AgentWorkhour.Day day,
        String startTime,
        String endTime
) {
    public static AgentWorkhourResponse from(AgentWorkhour agentWorkhour) {
        return new AgentWorkhourResponse(
                agentWorkhour.getId(),
                agentWorkhour.getAgent().getId(),
                agentWorkhour.getDay(),
                agentWorkhour.getStartTime(),
                agentWorkhour.getEndTime()
        );
    }
}