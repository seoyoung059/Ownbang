package com.bangguddle.ownbang.domain.agent.mypage.dto;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import lombok.Builder;

@Builder
public record AgentMyPageResponse(
        Long userId,
        Long agentId,
        String officeNumber,
        String licenseNumber,
        String officeAddress,
        String greetings,
        String officeName
) {
    public static AgentMyPageResponse from(Agent agent) {
        return AgentMyPageResponse.builder()
                .agentId(agent.getId())
                .userId(agent.getUser().getId())
                .officeNumber(agent.getOfficeNumber())
                .licenseNumber(agent.getLicenseNumber())
                .officeAddress(agent.getOfficeAddress())
                .greetings(agent.getGreetings())
                .officeName(agent.getOfficeName())
                .build();
    }
}
