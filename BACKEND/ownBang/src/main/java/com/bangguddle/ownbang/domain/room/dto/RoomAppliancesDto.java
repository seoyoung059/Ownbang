package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;

public record RoomAppliancesDto(
        boolean refrigerator,
        boolean washingMachine,
        boolean airConditioner,
        boolean bed,
        boolean desk,
        boolean microwave,
        boolean closet,
        boolean chair
) {


    public RoomAppliancesDto of (
            boolean refrigerator,
            boolean washingMachine,
            boolean airConditioner,
            boolean bed,
            boolean desk,
            boolean microwave,
            boolean closet,
            boolean chair) {
        return new RoomAppliancesDto(refrigerator, washingMachine, airConditioner,
                bed, desk, microwave, closet, chair);
    }


    public RoomAppliancesDto from (RoomAppliances roomAppliances) {
        return new RoomAppliancesDto(roomAppliances.isRefrigerator(),
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
