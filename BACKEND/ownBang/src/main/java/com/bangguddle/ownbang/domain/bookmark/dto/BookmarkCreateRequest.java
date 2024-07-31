package com.bangguddle.ownbang.domain.bookmark.dto;

import com.bangguddle.ownbang.domain.bookmark.entity.Bookmark;
import com.bangguddle.ownbang.domain.room.entity.Room;
import lombok.Builder;

@Builder
public record BookmarkCreateRequest(
        Long roomId,
        Long userId
) {
    static public BookmarkCreateRequest of(Long roomId, Long userId) {
        return BookmarkCreateRequest.builder()
                .roomId(roomId)
                .userId(userId)
                .build();
    }

    static public BookmarkCreateRequest from(Bookmark bookmark) {
        return BookmarkCreateRequest.builder()
                .roomId(bookmark.getRoom().getId())
//                .userId(bookmark.getUser().getId())
                .build();
    }

    public Bookmark toEntity(Room room/*, User user*/) {
        return Bookmark.builder()
                .room(room)
//                .user(user)
                .build();
    }
}
