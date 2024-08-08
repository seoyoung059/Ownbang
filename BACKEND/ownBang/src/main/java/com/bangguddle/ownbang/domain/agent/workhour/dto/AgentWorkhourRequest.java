package com.bangguddle.ownbang.domain.agent.workhour.dto;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;
import jakarta.validation.constraints.NotNull;

public record AgentWorkhourRequest(

        @NotNull
        String weekendStartTime,

        @NotNull
        String weekendEndTime,

        @NotNull
        String weekdayStartTime,

        @NotNull
        String weekdayEndTime

) {
    public AgentWorkhour toEntity(Agent agent) {
        return AgentWorkhour.builder()
                .agent(agent)
                .weekdayEndTime(weekdayEndTime)
                .weekendStartTime(weekendStartTime)
                .weekdayStartTime(weekdayStartTime)
                .weekendEndTime(weekendEndTime)
                .build();
    }

    public static AgentWorkhourRequest of( String weekdayStartTime, String weekdayEndTime,String weekendStartTime, String weekendEndTime) {
        return new AgentWorkhourRequest(weekdayStartTime, weekdayEndTime, weekendStartTime, weekendEndTime);
    }

    public static AgentWorkhourRequest from(AgentWorkhour agentWorkhour) {
        return new AgentWorkhourRequest(
                agentWorkhour.getWeekdayStartTime(),
                agentWorkhour.getWeekdayEndTime(),
                agentWorkhour.getWeekendStartTime(),
                agentWorkhour.getWeekendEndTime()
        );
    }

}