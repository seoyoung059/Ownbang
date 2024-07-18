package com.bangguddle.ownbang.domain.reservation;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(nullable = false)
    private Long roomId; // 매물 id

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private ReservationStatus status; //enum


    private Reservation(Long id, Long roomId, Long userId, LocalDateTime time, ReservationStatus status) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.time = time;
        this.status = status;
    }
    public static Reservation createReservation(Long id, Long roomId, Long userId, LocalDateTime time, ReservationStatus status) {
        return new Reservation( id,  roomId,  userId,  time,  status);
    }




}
