package com.bangguddle.ownbang.domain.auth.dto;

import com.bangguddle.ownbang.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserSignUpRequest(
        String name,
        String email,
        String password,
        String phoneNumber,
        String nickname
) {
    public User toEntity() {
        return User.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(password)
                .phoneNumber(phoneNumber)
                .build();
    }
}
