package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.enums.DealType;
import com.bangguddle.ownbang.domain.room.enums.RoomType;
import com.bangguddle.ownbang.domain.room.enums.Structure;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record RoomSelectResponse (
//        User agent,
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
//        @Valid
//        RoomAppliancesResponse roomAppliancesResponse,
//        @Valid
//        RoomDetailResponse roomDetailResponse,
        @Valid
        List<RoomImageResponse> roomImageResponses
) {

    static public RoomSelectResponse from(Room room) {
        return RoomSelectResponse.builder()
                .dealType(room.getDealType())
                .roomType(room.getRoomType())
                .structure(room.getStructure())
                .isLoft(room.isLoft())
                .exclusiveArea(room.getExclusiveArea())
                .supplyArea(room.getSupplyArea())
                .roomFloor(room.getRoomFloor())
                .deposit(room.getDeposit())
                .monthlyRent(room.getMonthlyRent())
                .maintenanceFee(room.getMaintenanceFee())
                .parcel(room.getParcel())
                .profileImageUrl(room.getProfileImageUrl())
//                .roomAppliancesResponse()
//                .roomAppliancesResponse()
                .roomImageResponses(room.getRoomImages().stream()
                        .map(RoomImageResponse::from)
                .collect(Collectors.toList()))
                .build();
    }
}
