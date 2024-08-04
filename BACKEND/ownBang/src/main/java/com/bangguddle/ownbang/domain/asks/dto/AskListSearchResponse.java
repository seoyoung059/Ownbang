package com.bangguddle.ownbang.domain.asks.dto;

import com.bangguddle.ownbang.domain.asks.entity.Ask;
import com.bangguddle.ownbang.domain.room.dto.RoomInfoSearchResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AskListSearchResponse(
        Long id,
//        UserInfo user,
        RoomInfoSearchResponse roomInfo,
        LocalDateTime lastSentTime
) {

    public AskListSearchResponse from (Ask ask) {
        return AskListSearchResponse.builder()
                .id(ask.getId())
                .roomInfo(RoomInfoSearchResponse.from(ask.getRoom()))
                .lastSentTime(ask.getLastSentTime())
//                .user(UserInfo.from(ask.getUser()))
                .build();
    }
}
