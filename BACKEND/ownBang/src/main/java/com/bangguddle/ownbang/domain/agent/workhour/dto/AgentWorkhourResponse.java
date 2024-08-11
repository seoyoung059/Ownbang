package com.bangguddle.ownbang.domain.agent.workhour.dto;

import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;

public record AgentWorkhourResponse(
        Long id,
        Long agentId,
        String weekdayStartTime,
        String weekdayEndTime,
        String weekendStartTime,
        String weekendEndTime
) {
    public static AgentWorkhourResponse from(AgentWorkhour agentWorkhour) {
        return new AgentWorkhourResponse(
                agentWorkhour.getId(),
                agentWorkhour.getAgent().getId(),
                agentWorkhour.getWeekdayStartTime(),
                agentWorkhour.getWeekdayEndTime(),
                agentWorkhour.getWeekendStartTime(),
                agentWorkhour.getWeekendEndTime()
        );
    }
}