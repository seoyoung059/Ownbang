package com.bangguddle.ownbang.domain.user.dto;

import com.bangguddle.ownbang.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserInfoResponse(
        Long userId,
        String nickname,
        String profileImageUrl
) {

    static public UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
