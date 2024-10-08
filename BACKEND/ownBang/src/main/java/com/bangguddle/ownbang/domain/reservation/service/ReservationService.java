package com.bangguddle.ownbang.domain.reservation.service;

import com.bangguddle.ownbang.domain.reservation.dto.*;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.springframework.stereotype.Service;

@Service
public interface ReservationService {

    SuccessResponse<NoneResponse> createReservation(Long userId, ReservationRequest reservationRequest);

    SuccessResponse<UserReservationListResponse> getMyReservationList (Long userId);

    SuccessResponse<NoneResponse> updateStatusReservation(Long userId, Long id) ;

    SuccessResponse<NoneResponse> deleteStatusReservation(Long userId, Long id) ;

    SuccessResponse<NoneResponse> confirmStatusReservation(Long userId, Long id);

    SuccessResponse<ReservationListResponse> getAgentReservations(Long userId);

    SuccessResponse<AvailableTimeResponse> getAvailableTimes(AvailableTimeRequest request);
}

