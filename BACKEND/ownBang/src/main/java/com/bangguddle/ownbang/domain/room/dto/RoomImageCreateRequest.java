package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomImage;
import lombok.Builder;

@Builder
public record RoomImageCreateRequest(
    Room room,
    String roomImageUrl
) {

    static public RoomImageCreateRequest of (Room room, String roomImageUrl){
        return RoomImageCreateRequest.builder()
                .room(room)
                .roomImageUrl(roomImageUrl)
                .build();
    }

    static public RoomImageCreateRequest from(RoomImage roomImage){
        return RoomImageCreateRequest.builder()
                .room(roomImage.getRoom())
                .roomImageUrl(roomImage.getRoomImageUrl())
                .build();
    }


    public RoomImage toEntity(Room room) {
        return RoomImage.builder()
                .room(room)
                .roomImageUrl(roomImageUrl)
                .build();
    }
}
