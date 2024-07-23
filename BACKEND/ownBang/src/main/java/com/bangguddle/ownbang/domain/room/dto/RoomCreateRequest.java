package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.enums.DealType;
import com.bangguddle.ownbang.domain.room.enums.RoomType;
import com.bangguddle.ownbang.domain.room.enums.Structure;
import lombok.Builder;

@Builder
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
        return RoomCreateRequest.builder()
//                .agent(agent)
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
                .roomAppliancesCreateRequest(roomAppliancesCreateRequest)
                .roomDetailCreateRequest(roomDetailCreateRequest)
                .build();
    }

    static public RoomCreateRequest from(Room room) {
        return RoomCreateRequest.builder()
                .dealType(room.getDealType())
                .roomType(room.getRoomType())
                .structure(room.getStructure())
                .isLoft(room.isLoft())
                .exclusiveArea(room.getExclusiveArea())
                .supplyArea(room.getSupplyArea())
                .roomFloor(room.getRoomFloor())
                .deposit(room.getDeposit())
                .monthlyRent(room.getMonthlyRent())
                .maintenanceFee(room.getMaintenanceFee())
                .parcel(room.getParcel())
                .profileImageUrl(room.getProfileImageUrl())
                .roomAppliancesCreateRequest(RoomAppliancesCreateRequest.from(room.getRoomAppliances()))
                .roomDetailCreateRequest(RoomDetailCreateRequest.from(room.getRoomDetail()))
                .build();
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

