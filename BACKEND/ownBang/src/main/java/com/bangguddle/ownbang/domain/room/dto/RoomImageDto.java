package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomImage;

public record RoomImageDto(
    Room room,
    String roomImageUrl
) {

    public RoomImageDto of (Room room, String roomImageUrl){
        return new RoomImageDto(room, roomImageUrl);
    }

    public RoomImageDto from(RoomImage roomImage){
        return new RoomImageDto(roomImage.getRoom(), roomImage.getRoomImageUrl());
    }


    public RoomImage toEntity(Room room) {
        return RoomImage.builder()
                .room(room)
                .roomImageUrl(roomImageUrl)
                .build();
    }
}
