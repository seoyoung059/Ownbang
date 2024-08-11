package com.bangguddle.ownbang.domain.review.dto;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.review.entity.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record ReviewCreateRequest(
        Long reservationId,
        Long agentId,
        @PositiveOrZero
        Integer starRating,
        @NotBlank
        String content
) {

    public Review toEntity(Reservation reservation, Agent agent) {
        return Review.builder()
                .reservation(reservation)
                .agent(agent)
                .starRating(starRating)
                .content(content)
                .build();
    }
}
