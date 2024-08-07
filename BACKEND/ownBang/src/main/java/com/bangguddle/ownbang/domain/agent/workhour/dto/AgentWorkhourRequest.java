package com.bangguddle.ownbang.domain.agent.workhour.dto;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;
import jakarta.validation.constraints.NotNull;

public record AgentWorkhourRequest(

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

    public static AgentWorkhourRequest of( AgentWorkhour.Day day, String startTime, String endTime) {
        return new AgentWorkhourRequest( day, startTime, endTime);
    }

    public static AgentWorkhourRequest from(AgentWorkhour agentWorkhour) {
        return new AgentWorkhourRequest(
                agentWorkhour.getDay(),
                agentWorkhour.getStartTime(),
                agentWorkhour.getEndTime()
        );
    }

}