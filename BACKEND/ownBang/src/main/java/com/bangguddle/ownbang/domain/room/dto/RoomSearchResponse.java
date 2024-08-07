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
public record RoomSearchResponse(
//        User agent,
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
        String road,
        String detailAddress,
        String profileImageUrl,
        RoomAppliancesSearchResponse roomAppliancesSearchResponse,
        RoomDetailSearchResponse roomDetailSearchResponse,
        @Valid
        List<RoomImageResponse> roomImageResponses
) {

    static public RoomSearchResponse from(Room room) {
        return RoomSearchResponse.builder()
                .latitude(room.getLatitude())
                .longitude(room.getLongitude())
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
                .road(room.getRoad())
                .detailAddress(room.getDetailAddress())
                .profileImageUrl(room.getProfileImageUrl())
                .roomAppliancesSearchResponse(RoomAppliancesSearchResponse.from(room.getRoomAppliances()))
                .roomDetailSearchResponse(RoomDetailSearchResponse.from(room.getRoomDetail()))
                .roomImageResponses(room.getRoomImages().stream()
                        .map(RoomImageResponse::from)
                .collect(Collectors.toList()))
                .build();
    }
}
