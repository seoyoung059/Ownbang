package com.bangguddle.ownbang.domain.agent.mypage.dto;

import jakarta.validation.constraints.NotNull;

public record AgentMyPageModifyRequest(
        @NotNull
        String officeNumber,
        @NotNull
        String officeAddress,
        @NotNull
        String greetings,
        @NotNull
        String officeName,
        @NotNull
        String licenseNumber,
        @NotNull
        String detailOfficeAddress,
        @NotNull
        String weekendStartTime,
        @NotNull
        String weekendEndTime,
        @NotNull
        String weekdayStartTime,
        @NotNull
        String weekdayEndTime
) {
}
