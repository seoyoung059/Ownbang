package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.enums.Facing;
import com.bangguddle.ownbang.domain.room.enums.HeatingType;
import com.bangguddle.ownbang.domain.room.enums.Purpose;
import lombok.Builder;

import java.util.Date;

@Builder
public record RoomDetailSearchResponse(
        Long id,
        Byte roomCount,
        Byte bathroomCount,
        HeatingType heatingType,
        Date moveInDate,
        Long buildingFloor,
        Boolean elevator,
        Long totalParking,
        Float parking,
        Date approvalDate,
        Date firstRegistrationDate,
        Facing facing,
        Purpose purpose,
        String road,
        String detailAddress
) {

    static public RoomDetailSearchResponse of(
            Long id,
            byte roomCount,
            byte bathroomCount,
            HeatingType heatingType,
            Date moveInDate,
            Long buildingFloor,
            boolean elevator,
            Long totalParking,
            float parking,
            Date approvalDate,
            Date firstRegistrationDate,
            Facing facing,
            Purpose purpose,
            String road,
            String detailAddress){
        return RoomDetailSearchResponse.builder()
                .id(id)
                .roomCount(roomCount)
                .bathroomCount(bathroomCount)
                .heatingType(heatingType)
                .moveInDate(moveInDate)
                .buildingFloor(buildingFloor)
                .elevator(elevator)
                .totalParking(totalParking)
                .parking(parking)
                .approvalDate(approvalDate)
                .firstRegistrationDate(firstRegistrationDate)
                .facing(facing)
                .purpose(purpose)
                .road(road)
                .detailAddress(detailAddress)
                .build();
    }

    static public RoomDetailSearchResponse from(RoomDetail roomDetail){
        return RoomDetailSearchResponse.builder()
                .id(roomDetail.getId())
                .roomCount(roomDetail.getRoomCount())
                .bathroomCount(roomDetail.getBathroomCount())
                .heatingType(roomDetail.getHeatingType())
                .moveInDate(roomDetail.getMoveInDate())
                .buildingFloor(roomDetail.getBuildingFloor())
                .elevator(roomDetail.isElevator())
                .totalParking(roomDetail.getTotalParking())
                .parking(roomDetail.getParking())
                .approvalDate(roomDetail.getApprovalDate())
                .firstRegistrationDate(roomDetail.getFirstRegistrationDate())
                .facing(roomDetail.getFacing())
                .purpose(roomDetail.getPurpose())
                .road(roomDetail.getRoad())
                .detailAddress(roomDetail.getDetailAddress())
                .build();
    }

    public RoomDetail toEntity() {
        return RoomDetail.builder()
                .roomCount(roomCount)
                .bathroomCount(bathroomCount)
                .heatingType(heatingType)
                .moveInDate(moveInDate)
                .buildingFloor(buildingFloor)
                .elevator(elevator)
                .totalParking(totalParking)
                .parking(parking)
                .approvalDate(approvalDate)
                .firstRegistrationDate(firstRegistrationDate)
                .facing(facing)
                .purpose(purpose)
                .road(road)
                .detailAddress(detailAddress)
                .build();
    }
}