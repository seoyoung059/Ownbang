package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomImage;
import lombok.Builder;

@Builder
public record RoomImageUpdateRequest(
        Long id,
        String roomImageUrl,
        Boolean isDeleted
) {

    static public RoomImageUpdateRequest of (Room room, String roomImageUrl, Boolean isDeleted){
        return RoomImageUpdateRequest.builder()
                .id(room.getId())
                .roomImageUrl(roomImageUrl)
                .isDeleted(isDeleted)
                .build();
    }

    static public RoomImageUpdateRequest from(RoomImage roomImage){
        return RoomImageUpdateRequest.builder()
                .id(roomImage.getId())
                .roomImageUrl(roomImage.getRoomImageUrl())
                .isDeleted(false)
                .build();
    }


    public RoomImage toEntity(Room room) {
        return RoomImage.builder()
                .roomImageUrl(roomImageUrl)
                .build();
    }
}
