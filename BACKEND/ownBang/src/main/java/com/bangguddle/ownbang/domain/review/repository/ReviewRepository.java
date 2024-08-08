package com.bangguddle.ownbang.domain.review.repository;


import com.bangguddle.ownbang.domain.review.entity.Review;
import com.bangguddle.ownbang.global.handler.AppException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.REVIEW_NOT_FOUND;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByAgentId(Long agentId);

    Optional<Review> findByReservationId(Long reservationId);

//    @Query("SELECT AVG(r.star_rating) From Review r where r.agent_id = :agentId")
//    Double findAverageStarRatingByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT AVG(r.starRating) FROM Review r WHERE r.agent.id = :agentId")
    Double calculateAverageStarRatingByAgentId(@Param("agentId") Long agentId);

    default Review getById(Long reviewId) {
        return findByReservationId(reviewId).orElseThrow(()->new AppException(REVIEW_NOT_FOUND));
    }
}
