package com.bangguddle.ownbang.domain.review.service.impl;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.repository.AgentRepository;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.review.dto.ReviewCreateRequest;
import com.bangguddle.ownbang.domain.review.dto.ReviewSearchResponse;
import com.bangguddle.ownbang.domain.review.entity.Review;
import com.bangguddle.ownbang.domain.review.repository.ReviewRepository;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements com.bangguddle.ownbang.domain.review.service.ReviewService {

    private final ReviewRepository reviewRepository;
    private final AgentRepository agentRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public SuccessResponse<List<ReviewSearchResponse>> getAgentReviews(Long agentId) {
        agentRepository.getById(agentId);
        List<ReviewSearchResponse> reviewList = reviewRepository.findByAgentId(agentId)
                .stream()
                .map(ReviewSearchResponse::from)
                .toList();
        return  new SuccessResponse<>(SuccessCode.REVIEW_FIND_SUCCESS, reviewList);
    }

    @Override
    public SuccessResponse<NoneResponse> createReview(Long userId, ReviewCreateRequest request) {
        User user = userRepository.getById(userId);
        Reservation reservation = reservationRepository.getById(request.reservationId());
        Agent agent = agentRepository.getById(request.agentId());
        if(!reservation.getUser().getId().equals(user.getId())) throw new AppException(ErrorCode.ACCESS_DENIED);

        if(reviewRepository.findByReservationId(request.reservationId()).isPresent()) throw new AppException(ErrorCode.REVIEW_DUPLICATED);
        Review review = request.toEntity(reservation, agent);

        reviewRepository.save(review);
        return new SuccessResponse<>(SuccessCode.REVIEW_CREATE_SUCCESS, NoneResponse.NONE);
    }

    @Override
    public SuccessResponse<NoneResponse> deleteReview(Long userId, Long reviewId) {
        userRepository.getById(userId);
        Review review = reviewRepository.getById(reviewId);
        if(!review.getReservation().getUser().getId().equals(userId)) throw new AppException(ErrorCode.ACCESS_DENIED);

        reviewRepository.delete(review);
        return new SuccessResponse<>(SuccessCode.REVIEW_DELETE_SUCCESS, NoneResponse.NONE);
    }

    @Override
    public SuccessResponse<Double> getAverageRating(Long agentId) {
        agentRepository.getById(agentId);
        return new SuccessResponse<>(SuccessCode.REVIEW_FIND_SUCCESS, reviewRepository.calculateAverageStarRatingByAgentId(agentId));
    }
}
