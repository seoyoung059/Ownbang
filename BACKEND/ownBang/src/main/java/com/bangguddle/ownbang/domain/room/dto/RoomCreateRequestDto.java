package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.entity.RoomImage;
import com.bangguddle.ownbang.domain.room.enums.DealType;
import com.bangguddle.ownbang.domain.room.enums.RoomType;
import com.bangguddle.ownbang.domain.room.enums.Structure;
import org.aspectj.weaver.loadtime.Agent;

import java.util.List;

public record RoomCreateRequestDto(
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
        RoomAppliancesDto roomAppliancesDto,
        RoomDetailDto roomDetailDto,
        List<RoomImageDto> roomImageDtoList
) {
    public RoomCreateRequestDto of(
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
            RoomAppliancesDto roomAppliancesDto,
            RoomDetailDto roomDetailDto,
            List<RoomImageDto> roomImageDtoList) {
        return new RoomCreateRequestDto(
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
                roomAppliancesDto,
                roomDetailDto,
                roomImageDtoList);
    }

    public RoomCreateRequestDto from(Room room, Agent agent, RoomAppliancesDto roomAppliancesDto, RoomDetailDto roomDetailDto, List<RoomImageDto> roomImageDtoList) {
        return new RoomCreateRequestDto(
//                agent,
                room.getDealType(),
                room.getRoomType(),
                room.getStructure(),
                room.isLoft(),
                room.getExclusiveArea(),
                room.getSupplyArea(),
                room.getRoomFloor(),
                room.getDeposit(),
                room.getMonthlyRent(),
                room.getMaintenanceFee(),
                room.getParcel(),
                room.getProfileImageUrl(),
                roomAppliancesDto,
                roomDetailDto,
                roomImageDtoList
        );
    }

    public Room toEntity(/*User agent, */RoomAppliances roomAppliances, RoomDetail roomDetail, List<RoomImage> roomImages) {
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
                .roomImages(roomImages)
                .build();
    }

}

