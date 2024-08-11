package com.bangguddle.ownbang.domain.user.dto;

import com.bangguddle.ownbang.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserBasicInfoResponse(
        Long userId,
        String nickname,
        String profileImageUrl
) {

    static public UserBasicInfoResponse from(User user) {
        return UserBasicInfoResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
