package com.bangguddle.ownbang.domain.asks.dto;

import com.bangguddle.ownbang.domain.asks.entity.AskContent;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AskContentSearchResponse(
        Long senderId,
        Long askId,
        String content,
        LocalDateTime sentTime,
        Boolean isRead
) {

    public AskContentSearchResponse from(AskContent askContent) {
        return AskContentSearchResponse.builder()
                .senderId(askContent.getSender().getId())
                .askId(askContent.getId())
                .content(askContent.getContent())
                .sentTime(askContent.getSentTime())
                .isRead(askContent.getIsRead())
                .build();
    }
}
