package com.bangguddle.ownbang.domain.bookmark.dto;

import com.bangguddle.ownbang.domain.bookmark.entity.Bookmark;
import com.bangguddle.ownbang.domain.room.dto.RoomInfoSearchResponse;
import lombok.Builder;

@Builder
public record BookmarkSearchResponse(
        Long id,
        Long userId,
        RoomInfoSearchResponse roomInfoSearchResponse
) {

    static public BookmarkSearchResponse from(Bookmark bookmark) {
        return BookmarkSearchResponse.builder()
                .id(bookmark.getId())
                .userId(bookmark.getUser().getId())
                .roomInfoSearchResponse(RoomInfoSearchResponse.from(bookmark.getRoom(), true))
                .build();
    }

}
