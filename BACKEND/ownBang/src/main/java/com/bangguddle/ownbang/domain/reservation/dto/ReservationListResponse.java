package com.bangguddle.ownbang.domain.reservation.dto;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;

import java.util.List;

public record ReservationListResponse (
        List<ReservationResponse> reservations
){
    public static ReservationListResponse from(List<ReservationResponse> reservations) {
        return new ReservationListResponse(reservations);
    }
}
