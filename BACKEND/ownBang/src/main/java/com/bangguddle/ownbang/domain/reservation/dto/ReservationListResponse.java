package com.bangguddle.ownbang.domain.reservation.dto;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;

import java.util.List;

public record ReservationListResponse (
        List<ReservationResponse> reservations
){
    public static ReservationListResponse from(List<Reservation> reservations) {
        List<ReservationResponse> reservationResponses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();
        return new ReservationListResponse(reservationResponses);
    }
}
