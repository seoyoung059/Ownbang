package com.bangguddle.ownbang.domain.reservation.service;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationRequest;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.springframework.stereotype.Service;

@Service
public interface ReservationService {

    SuccessResponse<NoneResponse> createReservation(ReservationRequest reservationRequest);

    SuccessResponse<ReservationListResponse> getMyReservationList (Long userId);

}

