package com.bangguddle.ownbang.domain.room.dto;

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

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record RoomUpdateRequest(
        Long id,
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
        @Valid
        RoomAppliancesUpdateRequest roomAppliancesUpdateRequest,
        @Valid
        RoomDetailUpdateRequest roomDetailUpdateRequest,
        List<RoomImageUpdateRequest> roomImageUpdateRequestList
) {

    static public RoomUpdateRequest of(
            Long id,
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
            String road,
            String detailAddress,
            RoomAppliancesUpdateRequest roomAppliancesUpdateRequest,
            RoomDetailUpdateRequest roomDetailUpdateRequest,
            List<RoomImageUpdateRequest> roomImageUpdateRequestList
    ) {
        return RoomUpdateRequest.builder()
                .id(id)
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
                .road(road)
                .detailAddress(detailAddress)
                .roomAppliancesUpdateRequest(roomAppliancesUpdateRequest)
                .roomDetailUpdateRequest(roomDetailUpdateRequest)
                .roomImageUpdateRequestList(roomImageUpdateRequestList)
                .build();
    }

    static public RoomUpdateRequest from(Room room) {
        return RoomUpdateRequest.builder()
                .id(room.getId())
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
                .roomAppliancesUpdateRequest(com.bangguddle.ownbang.domain.room.dto.RoomAppliancesUpdateRequest.from(room.getRoomAppliances()))
                .roomDetailUpdateRequest(RoomDetailUpdateRequest.from(room.getRoomDetail()))
                .roomImageUpdateRequestList(
                        room.getRoomImages().stream()
                                .map(RoomImageUpdateRequest::from)
                                .collect(Collectors.toList())
                )
                .build();
    }


    public Room toEntity(Room room, RoomAppliances roomAppliances, RoomDetail roomDetail) {
        return Room.builder()
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
                .road(road)
                .detailAddress(detailAddress)
                .roomAppliances(roomAppliances)
                .roomDetail(roomDetail)
                .build();
    }

}

