package com.bangguddle.ownbang.domain.asks.dto;

import com.bangguddle.ownbang.domain.asks.entity.Ask;
import com.bangguddle.ownbang.domain.room.dto.RoomInfoSearchResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AskSearchResponse(
        Long id,
//        UserInfo user,
        RoomInfoSearchResponse roomInfo,
        LocalDateTime lastSentTime,
        List<AskContentSearchResponse> askContentList
) {

    public AskSearchResponse from (Ask ask, List<AskContentSearchResponse> askContentList) {
        return AskSearchResponse.builder()
                .id(ask.getId())
                .roomInfo(RoomInfoSearchResponse.from(ask.getRoom()))
                .lastSentTime(ask.getLastSentTime())
//                .user(UserInfo.from(ask.getUser()))
                .askContentList(askContentList)
                .build();
    }
}