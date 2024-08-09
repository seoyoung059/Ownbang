package com.bangguddle.ownbang.domain.reservation.dto;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.user.dto.UserReservationInfoResponse;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        String AgentOfficeName,
        LocalDateTime reservationTime,
        ReservationStatus status,
        Long roomId,
        UserReservationInfoResponse userReservationInfoResponse,
        String roomProfileImage,
        Boolean enstance
) {
    public static ReservationResponse from(Reservation reservation,Boolean enstance) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getRoom().getAgent().getOfficeName(),
                reservation.getReservationTime(),
                reservation.getStatus(),
                reservation.getRoom().getId(),
                UserReservationInfoResponse.from(reservation.getUser()),
                reservation.getRoom().getProfileImageUrl(),
                enstance
        );
    }
}