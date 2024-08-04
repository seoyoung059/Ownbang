package com.bangguddle.ownbang.domain.asks.dto;

import com.bangguddle.ownbang.domain.asks.entity.Ask;
import com.bangguddle.ownbang.domain.asks.entity.AskContent;
import com.bangguddle.ownbang.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AskContentCreateRequest (
        Long askId,
        String content
){

    public AskContent toEntity(Ask ask, User sender, LocalDateTime sentTime) {
        return AskContent.builder()
                .ask(ask)
                .sender(sender)
                .content(content)
                .sentTime(sentTime)
                .isRead(false)
                .build();
    }
}
