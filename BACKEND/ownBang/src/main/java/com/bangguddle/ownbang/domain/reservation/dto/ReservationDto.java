package com.bangguddle.ownbang.domain.reservation.dto;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationDto(
        Long id,
        Long roomId,
        Long userId,
        LocalDateTime time,
        ReservationStatus status

) {

    public static ReservationDto of( Long id, Long roomId, Long userId, LocalDateTime time, ReservationStatus status) {
        return new ReservationDto(id, roomId, userId, time, status);
    }

    public static ReservationDto from(Reservation reservation){
        return new ReservationDto(
                reservation.getId(),
                reservation.getRoomId(),
                reservation.getUserId(),
                reservation.getTime(),
                reservation.getStatus()
        );
    }


    public Reservation toEntity() {
        return Reservation.builder()
                .id(id)
                .roomId(roomId)
                .userId(userId)
                .time(time)
                .status(status)
                .build();
    }
}