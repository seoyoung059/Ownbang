package com.bangguddle.ownbang.domain.user.dto;

import com.bangguddle.ownbang.domain.user.entity.User;
import lombok.Builder;

@Builder
public record
UserReservationInfoResponse(
        Long userId,
        String userName,
        String nickname,
        String phoneNumber
) {
    static public UserReservationInfoResponse from(User user) {
        return UserReservationInfoResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
