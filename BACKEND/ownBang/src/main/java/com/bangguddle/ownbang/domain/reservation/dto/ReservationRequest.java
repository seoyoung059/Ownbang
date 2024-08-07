package com.bangguddle.ownbang.domain.reservation.dto;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ReservationRequest(
        @NotNull
        @Positive
        Long roomId,
        @NotNull
        @Positive
        Long userId,
        @NotNull
        LocalDateTime reservationTime,
        @NotNull
        ReservationStatus status

) {
    public Reservation toEntity(Room room, User user) {
        return Reservation.builder()
                .room(room)
                .user(user)
                .reservationTime(reservationTime)
                .status(status)
                .build();
    }

    public static ReservationRequest of(long roomId, long userId, LocalDateTime reservationTime, ReservationStatus status) {
        return new ReservationRequest(roomId, userId, reservationTime, status);
    }

    public static ReservationRequest from(Reservation reservation) {
        return new ReservationRequest(
                reservation.getRoom().getId(),
                reservation.getUser().getId(),
                reservation.getReservationTime(),
                reservation.getStatus()
        );
    }

}