package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomImage;
import lombok.Builder;

@Builder
public record RoomImageUpdateRequest(
        Long id,
        Long roomId,
        String roomImageUrl,
        Boolean isDeleted
) {

    static public RoomImageUpdateRequest of (Long roomImageId, Long roomId, String roomImageUrl, Boolean isDeleted){
        return RoomImageUpdateRequest.builder()
                .id(roomImageId)
                .roomId(roomId)
                .roomImageUrl(roomImageUrl)
                .isDeleted(isDeleted)
                .build();
    }

    static public RoomImageUpdateRequest from(RoomImage roomImage){
        return RoomImageUpdateRequest.builder()
                .id(roomImage.getId())
                .roomId(roomImage.getRoom().getId())
                .roomImageUrl(roomImage.getRoomImageUrl())
                .isDeleted(false)
                .build();
    }


    public RoomImage toEntity(Room room) {
        return RoomImage.builder()
                .room(room)
                .roomImageUrl(roomImageUrl)
                .build();
    }
}
