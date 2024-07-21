package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;

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
        return new RoomAppliancesCreateRequest(refrigerator, washingMachine, airConditioner,
                bed, desk, microwave, closet, chair);
    }


    static public RoomAppliancesCreateRequest from (RoomAppliances roomAppliances) {
        return new RoomAppliancesCreateRequest(roomAppliances.isRefrigerator(),
                roomAppliances.isWashingMachine(),
                roomAppliances.isAirConditioner(),
                roomAppliances.isBed(),
                roomAppliances.isDesk(),
                roomAppliances.isMicrowave(),
                roomAppliances.isCloset(),
                roomAppliances.isChair());
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
