package com.bangguddle.ownbang.domain.reservation.entity;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //Lombok 어노테이션 : 기본 생성자
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false, updatable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @Column(name ="room_id",nullable = false, columnDefinition = "INT UNSIGNED")
    private Room room;

    @ManyToOne
    @Column(name ="user_id" ,nullable = false, columnDefinition = "INT UNSIGNED")
    private User user;

    @Column(nullable = false, columnDefinition = "DATETIME(0)")
    private LocalDateTime reservationTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;


    public Reservation withStatus() {
        return new Reservation(
                this.id,
                this.room,
                this.user,
                this.reservationTime,
                ReservationStatus.CANCELLED
        );
    }

    public Reservation confirmStatus() {
        return new Reservation(
                this.id,
                this.room,
                this.user,
                this.reservationTime,
                ReservationStatus.CONFIRMED
        );
    }
}
