package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import lombok.Builder;

@Builder
public record RoomAppliancesSearchResponse(
        Long id,
        Boolean refrigerator,
        Boolean washingMachine,
        Boolean airConditioner,
        Boolean bed,
        Boolean desk,
        Boolean microwave,
        Boolean closet,
        Boolean chair
) {


    static public RoomAppliancesSearchResponse of (
            Long id,
            Boolean refrigerator,
            Boolean washingMachine,
            Boolean airConditioner,
            Boolean bed,
            Boolean desk,
            Boolean microwave,
            Boolean closet,
            Boolean chair) {
        return RoomAppliancesSearchResponse.builder()
                .id(id)
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


    static public RoomAppliancesSearchResponse from (RoomAppliances roomAppliances) {
        return RoomAppliancesSearchResponse.builder()
                .id(roomAppliances.getId())
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
