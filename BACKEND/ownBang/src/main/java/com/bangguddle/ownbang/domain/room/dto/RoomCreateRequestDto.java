package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.enums.DealType;
import com.bangguddle.ownbang.domain.room.enums.RoomType;
import com.bangguddle.ownbang.domain.room.enums.Structure;

public record RoomCreateRequestDto(
        Long agentId,
        DealType dealType,
        RoomType roomType,
        Structure structure,
        boolean isLoft,
        float exclusiveArea,
        float supplyArea,
        byte roomFloor,
        Long deposit,
        Long monthlyRent,
        Long maintenanceFee,
        String parcel,
        String profileImageUrl,
        RoomAppliancesDto roomAppliancesDto,
        RoomDetailDto roomDetailDto
) {

    public Room toEntity(User agent, RoomAppliances roomAppliances, RoomDetail roomDetail) {
        return Room.builder()
                .agent(agent)
                .dealType(dealType)
                .roomType(roomType)
                .structure(structure)
                .isLoft(isLoft)
                .exclusiveArea(exclusiveArea)
                .supplyArea(supplyArea)
                .roomFloor(roomFloor)
                .deposit(deposit)
                .monthlyRent(monthlyRent)
                .maintenanceFee(maintenanceFee)
                .parcel(parcel)
                .profileImageUrl(profileImageUrl)
                .roomAppliances(roomAppliances)
                .roomDetail(roomDetail)
                .build();
    }

}

