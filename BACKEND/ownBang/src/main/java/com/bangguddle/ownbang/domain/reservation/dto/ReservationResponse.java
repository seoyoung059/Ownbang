package com.bangguddle.ownbang.domain.reservation.dto;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        LocalDateTime reservationTime,
        ReservationStatus status,
        Long roomId,
        Long userId
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getReservationTime(),
                reservation.getStatus(),
                reservation.getRoom().getId(),
                reservation.getUser().getId()
        );
    }
}