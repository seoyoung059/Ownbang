package com.bangguddle.ownbang.domain.review.dto;

import com.bangguddle.ownbang.domain.review.entity.Review;
import com.bangguddle.ownbang.domain.user.dto.UserBasicInfoResponse;
import lombok.Builder;

@Builder
public record ReviewSearchResponse(
        Long id,
        UserBasicInfoResponse userInfo,
        Long reservationId,
        Long agentId,
        Integer starRating,
        String content
) {

    static public ReviewSearchResponse from(Review review) {
        return ReviewSearchResponse.builder()
                .id(review.getId())
                .userInfo(UserBasicInfoResponse.from(review.getReservation().getUser()))
                .reservationId(review.getReservation().getId())
                .agentId(review.getAgent().getId())
                .starRating(review.getStarRating())
                .content(review.getContent())
                .build();
    }
}
