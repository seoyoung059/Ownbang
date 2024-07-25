package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomImage;
import lombok.Builder;

@Builder
public record RoomImageResponse(
        String roomImageUrl
) {

    static public RoomImageResponse of (Room room, String roomImageUrl){
        return RoomImageResponse.builder()
                .roomImageUrl(roomImageUrl)
                .build();
    }

    static public RoomImageResponse from(RoomImage roomImage){
        return RoomImageResponse.builder()
                .roomImageUrl(roomImage.getRoomImageUrl())
                .build();
    }


    public RoomImage toEntity(Room room) {
        return RoomImage.builder()
                .roomImageUrl(roomImageUrl)
                .build();
    }
}
