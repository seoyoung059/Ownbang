package com.bangguddle.ownbang.domain.webrtc.entity;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Webrtc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "webrtc_id", nullable = false, updatable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private String session;

    @OneToMany(mappedBy = "webrtc")
    private List<WebrtcToken> webrtcTokens = new ArrayList<>();

    @Builder
    public Webrtc(Reservation reservation, String session){
        this.reservation = reservation;
        this.session = session;
    }
}
