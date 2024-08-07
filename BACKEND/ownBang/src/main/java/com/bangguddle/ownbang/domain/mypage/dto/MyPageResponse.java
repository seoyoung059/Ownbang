package com.bangguddle.ownbang.domain.mypage.dto;

import com.bangguddle.ownbang.domain.user.entity.User;
import lombok.Builder;

@Builder
public record MyPageResponse(
        Long id,
        String name,
        String email,
        String phoneNumber,
        String nickname,
        Boolean oauthKakao,
        String profileImageUrl,
        Boolean isAgent
) {
    public static MyPageResponse from(User user) {
        return MyPageResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .nickname(user.getNickname())
                .oauthKakao(user.isOauthKakao())
                .profileImageUrl(user.getProfileImageUrl())
                .isAgent(user.isAgent())
                .build();
    }
}
