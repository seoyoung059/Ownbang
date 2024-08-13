package com.bangguddle.ownbang.domain.agent.auth.dto;

import com.bangguddle.ownbang.domain.agent.entity.Agent;
import com.bangguddle.ownbang.domain.agent.workhour.entity.AgentWorkhour;
import com.bangguddle.ownbang.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record AgentSignUpRequest(
        @NotNull(message = "사무실 번호를 입력해 주세요.")
        @Length(max=11, message = "적절한 사무실 번호를 입력해 주세요. (최대 11자)")
        String officeNumber,
        @NotNull(message = "자격 번호를 입력해 주세요.")
        @Length(max = 20, message = "적절한 자격번호를 입력해 주세요. (최대 20자)")
        String licenseNumber,
        @NotNull(message = "사무실 주소를 입력해 주세요.")
        @Length(max = 255, message = "적절한 도로명 주소를 입력해 주세요. (최대 255자)")
        String officeAddress,
        @NotNull(message = "사무실 상세 주소를 입력해 주세요.")
        @Length(max = 255, message = "적절한 상세주소를 입력해 주세요. (최대 255자)")
        String detailOfficeAddress,
        @NotNull(message = "사무실 이름을 입력해 주세요.")
        @Length(max = 255, message = "적절한 사무실 이름를 입력해 주세요. (최대 255자)")
        String officeName,
        @NotNull(message = "주말 영업 시작시간을 입력해 주세요.")
        String weekendStartTime,
        @NotNull(message = "주말 영업 종료시간을 입력해 주세요.")
        String weekendEndTime,
        @NotNull(message = "평일 영업 시작시간을 입력해 주세요.")
        String weekdayStartTime,
        @NotNull(message = "평일 영업 종료시간을 입력해 주세요.")
        String weekdayEndTime
) {
    public Agent toAgentEntity(User user) {
        return Agent.builder()
                .officeAddress(officeAddress)
                .licenseNumber(licenseNumber)
                .officeNumber(officeNumber)
                .officeName(officeName)
                .detailOfficeAddress(detailOfficeAddress)
                .user(user)
                .build();
    }

    public AgentWorkhour toAgentWorkhourEntity(Agent agent) {
        return AgentWorkhour.builder()
                .agent(agent)
                .weekdayEndTime(weekdayEndTime)
                .weekendEndTime(weekendEndTime)
                .weekendStartTime(weekendStartTime)
                .weekdayStartTime(weekdayStartTime)
                .build();
    }
}
