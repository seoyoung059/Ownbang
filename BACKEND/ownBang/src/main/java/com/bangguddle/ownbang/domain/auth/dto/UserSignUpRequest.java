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
    public User toEntity(String hashedPassword) {
        return User.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(hashedPassword)
                .phoneNumber(phoneNumber)
                .build();
    }
}
