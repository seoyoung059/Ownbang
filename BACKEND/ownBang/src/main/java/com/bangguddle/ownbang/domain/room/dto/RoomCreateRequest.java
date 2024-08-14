package com.bangguddle.ownbang.domain.room.dto;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.room.entity.Room;
import com.bangguddle.ownbang.domain.room.entity.RoomAppliances;
import com.bangguddle.ownbang.domain.room.entity.RoomDetail;
import com.bangguddle.ownbang.domain.room.enums.DealType;
import com.bangguddle.ownbang.domain.room.enums.RoomType;
import com.bangguddle.ownbang.domain.room.enums.Structure;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

@Builder
public record RoomCreateRequest(
        @NotNull(message = "주소를 다시 검색하여 입력해 주세요.")
        Float latitude,
        @NotNull(message = "주소를 다시 검색하여 입력해 주세요.")
        Float longitude,
        @NotBlank(message = "거래 종류를 선택해주세요.")
        String dealType,
        @NotBlank(message = "매물 종류를 선택해주세요.")
        String roomType,
        @NotBlank(message = "구조를 선택해주세요.")
        String structure,
        @NotNull
        Boolean isLoft,
        @NotNull(message = "전용 면적을 입력해주세요.")
        @Digits(integer = 3, fraction = 2, message = "적절한 값을 입력해 주세요. (1000 미만, 소숫점 두자리 수 까지 가능)")
        @Positive(message = "양수만 입력 가능합니다.")
        Float exclusiveArea,
        @NotNull(message = "공급 면적을 입력해주세요.")
        @Digits(integer = 3, fraction = 2, message = "적절한 값을 입력해 주세요. (1000 미만, 소숫점 두자리 수 까지 가능)")
        @Positive(message = "양수만 입력 가능합니다.")
        Float supplyArea,
        @NotNull(message = "매물 층을 입력해 주세요.")
        @Range(min=-128, max=127, message = "적절한 값을 입력해 주세요. (-128 이상, 127 미만의 수)")
        Byte roomFloor,
        @NotNull(message = "보증금(또는 전세금)을 입력해주세요. (없을 경우 0을 입력해 주세요.)")
        @PositiveOrZero(message = "적절한 값을 입력해주세요.")
        Long deposit,
        @NotNull(message = "월세를 입력해주세요. (없을 경우 0을 입력해 주세요.)")
        @PositiveOrZero(message = "적절한 값을 입력해 주세요.")
        Long monthlyRent,
        @NotNull(message = "보증금을 입력해주세요. (없을 경우 0을 입력해 주세요.")
        @PositiveOrZero(message = "적절한 값을 입력해 주세요.")
        Long maintenanceFee,
        String parcel,
        @NotBlank(message = "도로명 주소를 입력해 주세요.")
        String road,
        String detailAddress,
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
            String road,
            String detailAddress,
            RoomAppliancesCreateRequest roomAppliancesCreateRequest,
            RoomDetailCreateRequest roomDetailCreateRequest
    ) {
        return RoomCreateRequest.builder()
                .latitude(latitude)
                .longitude(longitude)
                .dealType(dealType.getValue())
                .roomType(roomType.getValue())
                .structure(structure.getValue())
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
                .roomAppliancesCreateRequest(roomAppliancesCreateRequest)
                .roomDetailCreateRequest(roomDetailCreateRequest)
                .build();
    }

    public Room toEntity(Agent agent, RoomAppliances roomAppliances, RoomDetail roomDetail) {
        return Room.builder()
                .agent(agent)
                .latitude(latitude)
                .longitude(longitude)
                .dealType(DealType.fromValue(dealType))
                .roomType(RoomType.fromValue(roomType))
                .structure(Structure.fromValue(structure))
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

