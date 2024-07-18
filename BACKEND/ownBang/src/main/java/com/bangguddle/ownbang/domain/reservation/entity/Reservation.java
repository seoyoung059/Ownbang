package com.bangguddle.ownbang.domain.reservation.entity;

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

}
