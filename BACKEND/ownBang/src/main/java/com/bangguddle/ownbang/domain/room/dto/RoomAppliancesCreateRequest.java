package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import lombok.Builder;

@Builder
public record RoomAppliancesCreateRequest(
        boolean refrigerator,
        boolean washingMachine,
        boolean airConditioner,
        boolean bed,
        boolean desk,
        boolean microwave,
        boolean closet,
        boolean chair
) {


    static public RoomAppliancesCreateRequest of (
            boolean refrigerator,
            boolean washingMachine,
            boolean airConditioner,
            boolean bed,
            boolean desk,
            boolean microwave,
            boolean closet,
            boolean chair) {
        return RoomAppliancesCreateRequest.builder()
                .refrigerator(refrigerator)
                .washingMachine(washingMachine)
                .airConditioner(airConditioner)
                .bed(bed)
                .desk(desk)
                .microwave(microwave)
                .closet(closet)
                .chair(chair)
                .build()                ;
    }


    static public RoomAppliancesCreateRequest from (RoomAppliances roomAppliances) {
        return RoomAppliancesCreateRequest.builder()
                .refrigerator(roomAppliances.isRefrigerator())
                .washingMachine(roomAppliances.isWashingMachine())
                .airConditioner(roomAppliances.isAirConditioner())
                .bed(roomAppliances.isBed())
                .desk(roomAppliances.isDesk())
                .microwave(roomAppliances.isMicrowave())
                .closet(roomAppliances.isCloset())
                .chair(roomAppliances.isChair())
                .build();
    }

    public RoomAppliances toEntity() {
        return RoomAppliances.builder()
                .refrigerator(refrigerator)
                .washingMachine(washingMachine)
                .airConditioner(airConditioner)
                .bed(bed)
                .desk(desk)
                .microwave(microwave)
                .closet(closet)
                .chair(chair)
                .build();
    }
}
