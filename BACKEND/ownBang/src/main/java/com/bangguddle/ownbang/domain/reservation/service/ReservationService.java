package com.bangguddle.ownbang.domain.reservation.service;

import com.bangguddle.ownbang.domain.reservation.dto.ReservationDto;
import com.bangguddle.ownbang.domain.reservation.dto.ReservationListResponse;
import com.bangguddle.ownbang.global.response.MessageResponse;


public interface ReservationService {

    MessageResponse createReservation(ReservationDto reservationdto);

    ReservationListResponse getReservationsByUserId(long userId);
}

