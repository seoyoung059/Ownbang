package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomImage;

public record RoomImageCreateRequest(
    Room room,
    String roomImageUrl
) {

    static public RoomImageCreateRequest of (Room room, String roomImageUrl){
        return new RoomImageCreateRequest(room, roomImageUrl);
    }

    static public RoomImageCreateRequest from(RoomImage roomImage){
        return new RoomImageCreateRequest(roomImage.getRoom(), roomImage.getRoomImageUrl());
    }


    public RoomImage toEntity(Room room) {
        return RoomImage.builder()
                .room(room)
                .roomImageUrl(roomImageUrl)
                .build();
    }
}
