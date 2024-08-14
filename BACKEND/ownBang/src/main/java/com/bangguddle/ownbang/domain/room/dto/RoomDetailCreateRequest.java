package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.enums.Facing;
import com.bangguddle.ownbang.domain.room.enums.HeatingType;
import com.bangguddle.ownbang.domain.room.enums.Purpose;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.util.Date;

@Builder
public record RoomDetailCreateRequest(
        @NotNull(message = "방 수를 입력해주세요.")
        @Positive(message = "적절한 방 수를 입력해주세요.")
        Byte roomCount,
        @NotNull(message = "화장실 수를 입력해주세요.")
        @PositiveOrZero(message = "적절한 화장실 수를 입력해 주세요.")
        Byte bathroomCount,
        @NotBlank(message = "난방 방식을 선택해주세요.")
        String heatingType,
        @NotNull(message = "입주 날짜를 입력해주세요. 즉시입주일 시 오늘 날짜를 선택해주세요.")
        Date moveInDate,
        @NotNull(message = "전체 건물의 층 수를 입력해 주세요.")
        Long buildingFloor,
        Boolean elevator,
        @NotNull(message = "총 주차 수를 입력해주세요.")
        @PositiveOrZero(message = "적절한 총 주차 수를 입력해주세요.")
        Long totalParking,
        @NotNull(message = "주차 가능 비율을 입력해주세요.")
        @PositiveOrZero(message = "적절한 주차 가능 비율을 입력해주세요.")
        Float parking,
        Date approvalDate,
        Date firstRegistrationDate,
        @NotBlank(message = "매물의 방향을 입력해 주세요.")
        String facing,
        @NotBlank(message = "건물 용도를 입력해 주세요.")
        String purpose
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
            Purpose purpose){
        return RoomDetailCreateRequest.builder()
                .roomCount(roomCount)
                .bathroomCount(bathroomCount)
                .heatingType(heatingType.getValue())
                .moveInDate(moveInDate)
                .buildingFloor(buildingFloor)
                .elevator(elevator)
                .totalParking(totalParking)
                .parking(parking)
                .approvalDate(approvalDate)
                .firstRegistrationDate(firstRegistrationDate)
                .facing(facing.getValue())
                .purpose(purpose.getValue())
                .build();
    }

    static public RoomDetailCreateRequest from(RoomDetail roomDetail){
        return RoomDetailCreateRequest.builder()
                .roomCount(roomDetail.getRoomCount())
                .bathroomCount(roomDetail.getBathroomCount())
                .heatingType(roomDetail.getHeatingType().getValue())
                .moveInDate(roomDetail.getMoveInDate())
                .buildingFloor(roomDetail.getBuildingFloor())
                .elevator(roomDetail.isElevator())
                .totalParking(roomDetail.getTotalParking())
                .parking(roomDetail.getParking())
                .approvalDate(roomDetail.getApprovalDate())
                .firstRegistrationDate(roomDetail.getFirstRegistrationDate())
                .facing(roomDetail.getFacing().getValue())
                .purpose(roomDetail.getPurpose().getValue())
                .build();
    }

    public RoomDetail toEntity() {
        return RoomDetail.builder()
                .roomCount(roomCount)
                .bathroomCount(bathroomCount)
                .heatingType(HeatingType.fromValue(heatingType))
                .moveInDate(moveInDate)
                .buildingFloor(buildingFloor)
                .elevator(elevator)
                .totalParking(totalParking)
                .parking(parking)
                .approvalDate(approvalDate)
                .firstRegistrationDate(firstRegistrationDate)
                .facing(Facing.fromValue(facing))
                .purpose(Purpose.fromValue(purpose))
                .build();
    }
}