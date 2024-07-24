package com.bangguddle.ownbang.domain.reservation.dto;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationRequest(
        @NotNull
        @Positive
        Long roomId,
        @NotNull
        @Positive
        Long userId,
        @NotNull
        LocalDateTime time,
        @NotNull
        ReservationStatus status

) {
    public Reservation toEntity() {
        return Reservation.builder()
                .roomId(roomId)
                .userId(userId)
                .time(time)
                .status(status)
                .build();
    }

    public static ReservationRequest of(long roomId, long userId, LocalDateTime time, ReservationStatus status) {
        return new ReservationRequest(roomId, userId, time, status);
    }

    public static ReservationRequest from(Reservation reservation) {
        return new ReservationRequest(
                reservation.getRoomId(),
                reservation.getUserId(),
                reservation.getTime(),
                reservation.getStatus()
        );
    }
}