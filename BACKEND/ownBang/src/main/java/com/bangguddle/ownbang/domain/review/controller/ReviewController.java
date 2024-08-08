package com.bangguddle.ownbang.domain.review.controller;

import com.bangguddle.ownbang.domain.review.dto.ReviewCreateRequest;
import com.bangguddle.ownbang.domain.review.dto.ReviewSearchResponse;
import com.bangguddle.ownbang.domain.review.service.ReviewService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 중개인의 리뷰를 조회하는 컨트롤러 메서드
     *
     * @param agentId 리뷰를 조회할 중개인 ID
     * @return
     */
    @GetMapping("/search/{agentId}")
    public ResponseEntity<Response<List<ReviewSearchResponse>>> getAgentReviews(@PathVariable @Positive @Valid Long agentId) {
        return Response.success(reviewService.getAgentReviews(agentId));
    }

    /**
     * 예약 종료 후 리뷰를 생성하는 컨트롤러 메서드
     *
     * @param userId Token에서 추출하는 리뷰 작성자 임차인
     * @param reviewCreateRequest 리뷰 작성
     * @return
     */
    @PostMapping
    public ResponseEntity<Response<NoneResponse>> createReview(@AuthenticationPrincipal Long userId,
                                                               @RequestBody @Valid ReviewCreateRequest reviewCreateRequest) {
        return Response.success(reviewService.createReview(userId, reviewCreateRequest));
    }

    /**
     * 리뷰 삭제 컨트롤러 메서드
     *
     * @param userId Token에서 추출하는 리뷰 작성한 임차인
     * @param reviewId 삭제할 리뷰 ID
     * @return
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Response<NoneResponse>> deleteReview(@AuthenticationPrincipal Long userId,
                                                               @PathVariable @Positive @Valid Long reviewId) {
        return Response.success(reviewService.deleteReview(userId, reviewId));
    }

    /**
     * 중개인의 평점을 조회하는 메서드
     *
     * @param agentId
     * @return
     */
    @GetMapping("/search/avg/{agentId}")
    public ResponseEntity<Response<Double>> getAverageRatings(@PathVariable @Positive @Valid Long agentId) {
        return Response.success(reviewService.getAverageRating(agentId));
    }
}
