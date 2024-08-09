package com.bangguddle.ownbang.domain.agent.mypage.dto;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record AgentMyPageResponse(
        Long userId,
        Long agentId,
        String officeNumber,
        String licenseNumber,
        String officeAddress,
        String greetings,
        String officeName,
        String detailOfficeAddress,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime weekendStartTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime  weekendEndTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime  weekdayStartTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime  weekdayEndTime
) {
    public static AgentMyPageResponse from(Agent agent, AgentWorkhour agentWorkhour) {
        return AgentMyPageResponse.builder()
                .agentId(agent.getId())
                .userId(agent.getUser().getId())
                .officeNumber(agent.getOfficeNumber())
                .licenseNumber(agent.getLicenseNumber())
                .officeAddress(agent.getOfficeAddress())
                .greetings(agent.getGreetings())
                .officeName(agent.getOfficeName())
                .detailOfficeAddress(agent.getDetailOfficeAddress())
                .weekdayStartTime(LocalTime.parse(agentWorkhour.getWeekdayStartTime()))
                .weekdayEndTime(LocalTime.parse(agentWorkhour.getWeekdayEndTime()))
                .weekendEndTime(LocalTime.parse(agentWorkhour.getWeekendEndTime()))
                .weekendStartTime(LocalTime.parse(agentWorkhour.getWeekendStartTime()))
                .build();
    }

}
