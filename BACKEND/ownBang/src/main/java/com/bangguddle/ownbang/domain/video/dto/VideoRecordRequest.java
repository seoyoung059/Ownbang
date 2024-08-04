package com.bangguddle.ownbang.domain.video.dto;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.video.entity.Video;
import com.bangguddle.ownbang.domain.video.entity.VideoStatus;
import lombok.Builder;

@Builder
public record VideoRecordRequest(Long reservationId, String videoUrl, VideoStatus videoStatus) {

    public Video toEntity(Reservation reservation){
        return Video.builder()
                .reservation(reservation)
                .videoUrl(this.videoUrl)
                .videoStatus(this.videoStatus)
                .build();
    }
}