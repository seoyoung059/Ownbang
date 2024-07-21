package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.enums.Facing;
import com.bangguddle.ownbang.domain.room.enums.HeatingType;
import com.bangguddle.ownbang.domain.room.enums.Purpose;
import java.util.Date;

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
        return new RoomDetailCreateRequest(roomCount, bathroomCount, heatingType, moveInDate,
                buildingFloor, elevator, totalParking, parking, approvalDate, firstRegistrationDate,
                facing, purpose, road, detailAddress);
    }

    static public RoomDetailCreateRequest from(RoomDetail roomDetail){
        return new RoomDetailCreateRequest(
                roomDetail.getRoomCount(),
                roomDetail.getBathroomCount(),
                roomDetail.getHeatingType(),
                roomDetail.getMoveInDate(),
                roomDetail.getBuildingFloor(),
                roomDetail.isElevator(),
                roomDetail.getTotalParking(),
                roomDetail.getParking(),
                roomDetail.getApprovalDate(),
                roomDetail.getFirstRegistrationDate(),
                roomDetail.getFacing(),
                roomDetail.getPurpose(),
                roomDetail.getRoad(),
                roomDetail.getDetailAddress()
        );
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