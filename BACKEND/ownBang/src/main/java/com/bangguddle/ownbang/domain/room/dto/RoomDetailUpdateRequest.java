package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.enums.Facing;
import com.bangguddle.ownbang.domain.room.enums.HeatingType;
import com.bangguddle.ownbang.domain.room.enums.Purpose;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.util.Date;

@Builder
public record RoomDetailUpdateRequest(
        Long id,
        @Positive
        Byte roomCount,
        @PositiveOrZero
        Byte bathroomCount,
        HeatingType heatingType,
        Date moveInDate,
        Long buildingFloor,
        Boolean elevator,
        @PositiveOrZero
        Long totalParking,
        @PositiveOrZero
        Float parking,
        Date approvalDate,
        Date firstRegistrationDate,
        Facing facing,
        Purpose purpose
) {

    static public RoomDetailUpdateRequest of(
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
            Purpose purpose) {
        return RoomDetailUpdateRequest.builder()
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
                .build();
    }

    static public RoomDetailUpdateRequest from(RoomDetail roomDetail) {
        return RoomDetailUpdateRequest.builder()
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
                .build();
    }
}