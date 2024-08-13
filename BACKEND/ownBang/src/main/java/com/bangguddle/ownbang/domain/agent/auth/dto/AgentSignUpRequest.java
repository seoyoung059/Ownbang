package com.bangguddle.ownbang.domain.agent.auth.dto;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;
import com.bangguddle.ownbang.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AgentSignUpRequest(
        @NotNull
        String officeNumber,
        @NotNull
        String licenseNumber,
        @NotNull
        String officeAddress,
        @NotNull
        String detailOfficeAddress,
        @NotNull
        String officeName,

        String weekendStartTime,

        String weekendEndTime,

        String weekdayStartTime,

        String weekdayEndTime
) {
    public Agent toAgentEntity(User user) {
        return Agent.builder()
                .officeAddress(officeAddress)
                .licenseNumber(licenseNumber)
                .officeNumber(officeNumber)
                .officeName(officeName)
                .detailOfficeAddress(detailOfficeAddress)
                .user(user)
                .build();
    }

    public AgentWorkhour toAgentWorkhourEntity(Agent agent) {
        return AgentWorkhour.builder()
                .agent(agent)
                .weekdayEndTime(weekdayEndTime)
                .weekendEndTime(weekendEndTime)
                .weekendStartTime(weekendStartTime)
                .weekdayStartTime(weekdayStartTime)
                .build();
    }
}
