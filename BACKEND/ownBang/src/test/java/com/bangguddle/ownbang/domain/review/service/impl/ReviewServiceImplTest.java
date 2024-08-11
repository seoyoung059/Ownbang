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
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    @DisplayName("중개인 리뷰 목록 조회 - 성공")
    void getAgentReviews_Success() {
        //given
        Long agentId = 1L;
        Agent agent = mock(Agent.class);
        User user = mock(User.class);
        Reservation reservation1 = Reservation.builder().id(1L).user(user).build();
        Reservation reservation2 = Reservation.builder().id(1L).user(user).build();
        List<Review> reviews = new LinkedList<>();
        Review review1 = Review.builder().reservation(reservation1).agent(agent).build();
        Review review2 = Review.builder().reservation(reservation2).agent(agent).build();
        reviews.add(review1); reviews.add(review2);

        when(agentRepository.getById(agentId)).thenReturn(agent);
        when(reviewRepository.findByAgentId(agentId)).thenReturn(reviews);
        when(ReviewSearchResponse.from(review1)).thenReturn(any(ReviewSearchResponse.class));

        SuccessResponse<List<ReviewSearchResponse>> response = reviewService.getAgentReviews(agentId);

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(SuccessResponse.class);
        assertThat(response.successCode()).isEqualTo(REVIEW_FIND_SUCCESS);

        verify(reviewRepository, times(1)).findByAgentId(anyLong());
    }

    @Test
    @DisplayName("중개인 리뷰 목록 조회 - 실패: 존재하지 않는 중개인 ID")
    void getAgentReviews_Fail_InvalidAgent() {
        //given
        Long agentId = 1L;
        Agent agent = mock(Agent.class);

        when(agentRepository.getById(anyLong())).thenThrow(new AppException(ACCESS_DENIED));


        assertThatThrownBy(()->
                reviewService.getAgentReviews(agentId)
        ).isInstanceOf(AppException.class)
                .hasMessage(ACCESS_DENIED.getMessage());
    }


    @Test
    @DisplayName("리뷰 등록 - 성공")
    void createReview_Success() {
        Long userId = 1L, agentId = 10L, reservationId = 100L;
        User user = mock(User.class);
        Reservation reservation = Reservation.builder().id(reservationId).user(user).build();
        Agent agent = mock(Agent.class);
        ReviewCreateRequest request = ReviewCreateRequest.builder().agentId(agentId).starRating(4).reservationId(reservationId).build();

        when(user.getId()).thenReturn(userId);
        when(userRepository.getById(anyLong())).thenReturn(user);
        when(reviewRepository.findByReservationId(reservationId)).thenReturn(Optional.empty());
        when(reservationRepository.getById(anyLong())).thenReturn(reservation);

        SuccessResponse<NoneResponse> response = reviewService.createReview(userId, request);

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(SuccessResponse.class);
        assertThat(response.successCode()).isEqualTo(REVIEW_CREATE_SUCCESS);

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("리뷰 등록 - 실패: 존재하지 않는 유저")
    void createReview_Fail_NonExistUser() {
        Long userId = 1L, agentId = 10L, reservationId = 100L;
        ReviewCreateRequest request = ReviewCreateRequest.builder().agentId(agentId).starRating(4).reservationId(reservationId).build();

        when(userRepository.getById(anyLong())).thenThrow(new AppException(USER_NOT_FOUND));


        assertThatThrownBy(()->
                reviewService.createReview(userId, request)
        ).isInstanceOf(AppException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }


    @Test
    @DisplayName("리뷰 등록 - 실패: 존재하지 않는 예약")
    void createReview_Fail_InvalidReservation() {
        Long userId = 1L, agentId = 10L, reservationId = 100L;
        User user = mock(User.class);
        Reservation reservation = Reservation.builder().id(reservationId).user(user).build();
        Agent agent = mock(Agent.class);
        ReviewCreateRequest request = ReviewCreateRequest.builder().agentId(agentId).starRating(4).reservationId(reservationId).build();

        when(userRepository.getById(anyLong())).thenReturn(user);
        when(reservationRepository.getById(anyLong())).thenThrow(new AppException(RESERVATION_NOT_FOUND));

        assertThatThrownBy(()->
                reviewService.createReview(userId, request)
        ).isInstanceOf(AppException.class)
                .hasMessage(RESERVATION_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("리뷰 등록 - 실패: 권한이 없는 유저")
    void createReview_Fail_InvalidUser() {
        Long userId = 1L, reservationUserId = 11L, agentId = 10L, reservationId = 100L;
        User user = mock(User.class);
        User reservationUser = mock(User.class);
        Reservation reservation = Reservation.builder().id(reservationId).user(reservationUser).build();
        ReviewCreateRequest request = ReviewCreateRequest.builder().agentId(agentId).starRating(4).reservationId(reservationId).build();

        when(user.getId()).thenReturn(userId);
        when(reservationUser.getId()).thenReturn(reservationUserId);
        when(userRepository.getById(anyLong())).thenReturn(user);
        when(reservationRepository.getById(anyLong())).thenReturn(reservation);

        assertThatThrownBy(()->
                reviewService.createReview(userId, request)
        ).isInstanceOf(AppException.class)
                .hasMessage(ACCESS_DENIED.getMessage());
    }

    @Test
    @DisplayName("리뷰 등록 - 실패: 이미 리뷰가 작성된 예약")
    void createReview_Fail_DuplicatedReview() {
        Long userId = 1L, agentId = 10L, reservationId = 100L;
        User user = mock(User.class);
        Reservation reservation = Reservation.builder().id(reservationId).user(user).build();
        Agent agent = mock(Agent.class);
        Review duplicatedReview = mock(Review.class);
        ReviewCreateRequest request = ReviewCreateRequest.builder().agentId(agentId).starRating(4).reservationId(reservationId).build();

        when(user.getId()).thenReturn(userId);
        when(userRepository.getById(anyLong())).thenReturn(user);
        when(reviewRepository.findByReservationId(reservationId)).thenReturn(Optional.of(duplicatedReview));
        when(reservationRepository.getById(anyLong())).thenReturn(reservation);

        assertThatThrownBy(()->
                reviewService.createReview(userId, request)
        ).isInstanceOf(AppException.class)
                .hasMessage(REVIEW_DUPLICATED.getMessage());
    }

    @Test
    @DisplayName("리뷰 삭제 - 성공")
    void deleteReview_Success() {
        Long userId = 1L, reviewId = 10L;
        User user = mock(User.class);
        Reservation reservation = Reservation.builder().id(1L).user(user).build();
        Review review = Review.builder().reservation(reservation).build();

        when(userRepository.getById(anyLong())).thenReturn(user);
        when(reviewRepository.getById(anyLong())).thenReturn(review);
        when(user.getId()).thenReturn(userId);


        SuccessResponse<NoneResponse> response = reviewService.deleteReview(userId, reviewId);

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(SuccessResponse.class);
        assertThat(response.successCode()).isEqualTo(REVIEW_DELETE_SUCCESS);

        verify(reviewRepository, times(1)).delete(any(Review.class));

    }


    @Test
    @DisplayName("리뷰 삭제 - 실패: 존재하지 않는 유저")
    void deleteReview_Fail_UserNotFound() {
        Long userId = 1L, reviewId = 10L;
        User user = mock(User.class);
        Reservation reservation = Reservation.builder().id(1L).user(user).build();
        Review review = Review.builder().reservation(reservation).build();

        when(userRepository.getById(anyLong())).thenThrow(new AppException(USER_NOT_FOUND));


        assertThatThrownBy(()->
                reviewService.deleteReview(userId, reviewId)
        ).isInstanceOf(AppException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }


    @Test
    @DisplayName("리뷰 삭제 - 존재하지 않는 리뷰")
    void deleteReview_Fail_ReviewNotFound() {
        Long userId = 1L, reviewId = 10L;
        User user = mock(User.class);
        Reservation reservation = Reservation.builder().id(1L).user(user).build();
        Review review = Review.builder().reservation(reservation).build();

        when(userRepository.getById(anyLong())).thenReturn(user);
        when(reviewRepository.getById(anyLong())).thenThrow(new AppException(REVIEW_NOT_FOUND));



        assertThatThrownBy(()->
                reviewService.deleteReview(userId, reviewId)
        ).isInstanceOf(AppException.class)
                .hasMessage(REVIEW_NOT_FOUND.getMessage());
    }



    @Test
    @DisplayName("리뷰 삭제 - 권한 없는 이용자")
    void deleteReview_Fail_InvalidUser() {
        Long userId = 1L, reviewId = 10L, reviewUserId = 11L;
        User user = mock(User.class);
        Reservation reservation = Reservation.builder().id(1L).user(user).build();
        Review review = Review.builder().reservation(reservation).build();

        when(userRepository.getById(anyLong())).thenReturn(user);
        when(reviewRepository.getById(anyLong())).thenReturn(review);
        when(user.getId()).thenReturn(reviewUserId);



        assertThatThrownBy(()->
                reviewService.deleteReview(userId, reviewId)
        ).isInstanceOf(AppException.class)
                .hasMessage(ACCESS_DENIED.getMessage());
    }

    @Test
    @DisplayName("평점 조회 - 성공")
    void getAverageRating() {
        Long agentId = 1L; Double rating = 4.3;
        Agent agent = mock(Agent.class);

        when(agentRepository.getById(anyLong())).thenReturn(agent);
        when(reviewRepository.calculateAverageStarRatingByAgentId(agentId)).thenReturn(rating);

        SuccessResponse<Double> response = reviewService.getAverageRating(agentId);

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(SuccessResponse.class);
        assertThat(response.successCode()).isEqualTo(REVIEW_FIND_SUCCESS);
        assertThat(response.data()).isEqualTo(rating);

        verify(reviewRepository, times(1)).calculateAverageStarRatingByAgentId(anyLong());
    }

    @Test
    @DisplayName("평점 조회 - 실패: 존재하지 않는 중개인 ID")
    void getAverageRating_Fail_AgentNotFound() {
        Long agentId = 1L;

        when(agentRepository.getById(anyLong())).thenThrow(new AppException(ACCESS_DENIED));

        assertThatThrownBy(()->
                reviewService.getAverageRating(agentId)
        ).isInstanceOf(AppException.class)
                .hasMessage(ACCESS_DENIED.getMessage());
    }
}