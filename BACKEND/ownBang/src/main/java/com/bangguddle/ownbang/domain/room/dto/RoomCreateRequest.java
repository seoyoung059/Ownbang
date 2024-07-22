package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.enums.DealType;
import com.bangguddle.ownbang.domain.room.enums.RoomType;
import com.bangguddle.ownbang.domain.room.enums.Structure;

public record RoomCreateRequest(
//        User agent,
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
        RoomAppliancesCreateRequest roomAppliancesCreateRequest,
        RoomDetailCreateRequest roomDetailCreateRequest
) {

    static public RoomCreateRequest of(
//            User agent,
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
            RoomAppliancesCreateRequest roomAppliancesCreateRequest,
            RoomDetailCreateRequest roomDetailCreateRequest
    ) {
        return new RoomCreateRequest(
//                agent,
                dealType,
                roomType,
                structure,
                isLoft,
                exclusiveArea,
                supplyArea,
                roomFloor,
                deposit,
                monthlyRent,
                maintenanceFee,
                parcel,
                profileImageUrl,
                roomAppliancesCreateRequest,
                roomDetailCreateRequest
        );
    }

    static public RoomCreateRequest from(Room room) {
        return new RoomCreateRequest(/*room.getAgent(),*/room.getDealType(), room.getRoomType(),
                room.getStructure(), room.isLoft(), room.getExclusiveArea(), room.getSupplyArea(), room.getRoomFloor(),
                room.getDeposit(), room.getMonthlyRent(), room.getMaintenanceFee(), room.getParcel(),
                room.getProfileImageUrl(),
                RoomAppliancesCreateRequest.from(room.getRoomAppliances()),
                RoomDetailCreateRequest.from(room.getRoomDetail()));
    }


    public Room toEntity(/*User agent, */RoomAppliances roomAppliances, RoomDetail roomDetail) {
        return Room.builder()
                /*.agent(agent)*/
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

