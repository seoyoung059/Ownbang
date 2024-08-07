package com.bangguddle.ownbang.domain.agent.auth.dto;

import com.bangguddle.ownbang.domain.agent.entity.AgentWorkhour;
import com.bangguddle.ownbang.domain.agent.entity.Agent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AgentWorkhourRequest(
        @NotNull
        @Positive
        Long agentId,

        @NotNull
        AgentWorkhour.Day day,

        @NotNull
        String startTime,

        @NotNull
        String endTime
) {
    public AgentWorkhour toEntity(Agent agent) {
        return AgentWorkhour.builder()
                .agent(agent)
                .day(day)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public static AgentWorkhourRequest of(Long agentId, AgentWorkhour.Day day, String startTime, String endTime) {
        return new AgentWorkhourRequest(agentId, day, startTime, endTime);
    }

    public static AgentWorkhourRequest from(AgentWorkhour agentWorkhour) {
        return new AgentWorkhourRequest(
                agentWorkhour.getAgent().getId(),
                agentWorkhour.getDay(),
                agentWorkhour.getStartTime(),
                agentWorkhour.getEndTime()
        );
    }

}