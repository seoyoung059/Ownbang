package com.bangguddle.ownbang.domain.video.dto;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.video.entity.Video;
import com.bangguddle.ownbang.domain.video.entity.VideoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record VideoUpdateRequest(
        @NotBlank
        String videoUrl,

        @NotNull
        VideoStatus videoStatus
) { }