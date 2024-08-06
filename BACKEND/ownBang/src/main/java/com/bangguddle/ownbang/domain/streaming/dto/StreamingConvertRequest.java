package com.bangguddle.ownbang.domain.streaming.dto;

public record StreamingConvertRequest(
        String sessionId,
        Long reservationId
) {
}
