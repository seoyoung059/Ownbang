package com.bangguddle.ownbang.domain.video.entity;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "video")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video {

    @Id
    @Column(name = "video_id", nullable = false, columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private String videoUrl;

    @Column(nullable = false)
    private VideoStatus videoStatus;

    @Builder
    public Video(Reservation reservation, String videoUrl, VideoStatus videoStatus){
        this.reservation = reservation;
        this.videoUrl = videoUrl;
        this.videoStatus = videoStatus;
    }

    public void update(String videoUrl, VideoStatus videoStatus){
        this.videoUrl = videoUrl;
        this.videoStatus = videoStatus;
    }
}
