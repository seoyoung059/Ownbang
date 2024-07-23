package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.enums.Facing;
import com.bangguddle.ownbang.domain.room.enums.HeatingType;
import com.bangguddle.ownbang.domain.room.enums.Purpose;
import lombok.Builder;

import java.util.Date;

@Builder
public record RoomDetailCreateRequest(
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
        String detailAddress
) {

    static public RoomDetailCreateRequest of(
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
        return RoomDetailCreateRequest.builder()
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

    static public RoomDetailCreateRequest from(RoomDetail roomDetail){
        return RoomDetailCreateRequest.builder()
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