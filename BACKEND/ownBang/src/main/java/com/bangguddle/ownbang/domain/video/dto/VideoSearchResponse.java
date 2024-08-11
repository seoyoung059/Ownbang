package com.bangguddle.ownbang.domain.video.dto;

import com.bangguddle.ownbang.domain.video.entity.Video;
import com.bangguddle.ownbang.domain.video.entity.VideoStatus;
import lombok.Builder;

@Builder
public record VideoSearchResponse(Long videoId, Long reservationId, String videoUrl, VideoStatus videoStatus) {

    static public VideoSearchResponse from(Video video) {
        return VideoSearchResponse.builder()
                .videoId(video.getId())
                .reservationId(video.getReservation().getId())
                .videoUrl(video.getVideoUrl())
                .videoStatus(video.getVideoStatus())
                .build();
    }
}
