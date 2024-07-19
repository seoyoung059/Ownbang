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
