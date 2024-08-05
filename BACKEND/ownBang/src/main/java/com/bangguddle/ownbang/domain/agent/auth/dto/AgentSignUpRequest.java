package com.bangguddle.ownbang.domain.agent.auth.dto;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.user.entity.User;
import lombok.Builder;

@Builder
public record AgentSignUpRequest(
        String officeNumber,
        String licenseNumber,
        String officeAddress,
        String officeName
) {
    public Agent toEntity(User user) {
        return Agent.builder()
                .officeAddress(officeAddress)
                .licenseNumber(licenseNumber)
                .officeAddress(officeAddress)
                .officeName(officeName)
                .user(user)
                .build();
    }

}
