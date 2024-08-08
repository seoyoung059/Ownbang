package com.bangguddle.ownbang.domain.review.service;

import com.bangguddle.ownbang.domain.review.dto.ReviewCreateRequest;
import com.bangguddle.ownbang.domain.review.dto.ReviewSearchResponse;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

import java.util.List;

public interface ReviewService {
    SuccessResponse<List<ReviewSearchResponse>> getAgentReviews(Long agentId);

    SuccessResponse<NoneResponse> createReview(Long userId, ReviewCreateRequest request);

    SuccessResponse<NoneResponse> deleteReview(Long userId, Long reviewId);

    SuccessResponse<Double> getAverageRating(Long agentId);
}
