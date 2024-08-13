package com.bangguddle.ownbang.domain.agent.mypage.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record AgentMyPageModifyRequest(
        @NotNull(message = "사무실 번호를 입력해 주세요.")
        @Length(max=11, message = "적절한 사무실 번호를 입력해 주세요. (최대 11자)")
        String officeNumber,
        @NotNull(message = "사무실 주소를 입력해 주세요.")
        @Length(max = 255, message = "적절한 도로명 주소를 입력해 주세요. (최대 255자)")
        String officeAddress,
        String greetings,
        @NotNull(message = "사무실 이름을 입력해 주세요.")
        @Length(max = 255, message = "적절한 사무실 이름를 입력해 주세요. (최대 255자)")
        String officeName,
        @NotNull(message = "자격 번호를 입력해 주세요.")
        @Length(max = 20, message = "적절한 자격번호를 입력해 주세요. (최대 20자)")
        String licenseNumber,
        @NotNull(message = "사무실 상세 주소를 입력해 주세요.")
        @Length(max = 255, message = "적절한 상세주소를 입력해 주세요. (최대 255자)")
        String detailOfficeAddress,
        @NotNull(message = "주말 영업 시작시간을 입력해 주세요.")
        String weekendStartTime,
        @NotNull(message = "주말 영업 종료시간을 입력해 주세요.")
        String weekendEndTime,
        @NotNull(message = "평일 영업 시작시간을 입력해 주세요.")
        String weekdayStartTime,
        @NotNull(message = "평일 영업 종료시간을 입력해 주세요.")
        String weekdayEndTime
) {
}
