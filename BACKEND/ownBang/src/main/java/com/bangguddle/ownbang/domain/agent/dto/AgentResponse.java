package com.bangguddle.ownbang.domain.agent.dto;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import lombok.Builder;

@Builder
public record AgentResponse(
        Long agentId,
        String profileImage,
        String greeting,
        String officeName,
        Double starRating
) {

    static public AgentResponse from(Agent agent, Double starRating) {
        return AgentResponse.builder()
                .agentId(agent.getId())
                .profileImage(agent.getUser().getProfileImageUrl())
                .greeting(agent.getGreetings())
                .officeName(agent.getOfficeName())
                .starRating(starRating)
                .build();
    }
}
