package com.bangguddle.ownbang.domain.webrtc.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record WebrtcCreateRequest (
        @NotNull
        @Positive
        Long reservationId
) { }
