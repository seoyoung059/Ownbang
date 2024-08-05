package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.enums.DealType;
import com.bangguddle.ownbang.domain.room.enums.RoomType;
import com.bangguddle.ownbang.domain.room.enums.Structure;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record RoomCreateRequest(
        Float latitude,
        Float longitude,
        DealType dealType,
        RoomType roomType,
        Structure structure,
        Boolean isLoft,
        @Positive
        Float exclusiveArea,
        @Positive
        Float supplyArea,
        Byte roomFloor,
        @PositiveOrZero
        Long deposit,
        @PositiveOrZero
        Long monthlyRent,
        @PositiveOrZero
        Long maintenanceFee,
        String parcel,
        String profileImageUrl,
        @Valid
        RoomAppliancesCreateRequest roomAppliancesCreateRequest,
        @Valid
        RoomDetailCreateRequest roomDetailCreateRequest
) {

    static public RoomCreateRequest of(
            Float latitude,
            Float longitude,
            DealType dealType,
            RoomType roomType,
            Structure structure,
            Boolean isLoft,
            Float exclusiveArea,
            Float supplyArea,
            Byte roomFloor,
            Long deposit,
            Long monthlyRent,
            Long maintenanceFee,
            String parcel,
            String profileImageUrl,
            RoomAppliancesCreateRequest roomAppliancesCreateRequest,
            RoomDetailCreateRequest roomDetailCreateRequest
    ) {
        return RoomCreateRequest.builder()
                .latitude(latitude)
                .longitude(longitude)
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

    public Room toEntity(Agent agent, RoomAppliances roomAppliances, RoomDetail roomDetail) {
        return Room.builder()
                .agent(agent)
                .latitude(latitude)
                .longitude(longitude)
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

