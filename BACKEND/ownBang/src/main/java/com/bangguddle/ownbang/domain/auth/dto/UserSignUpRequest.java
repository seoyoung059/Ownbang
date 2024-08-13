package com.bangguddle.ownbang.domain.auth.dto;

import com.bangguddle.ownbang.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record UserSignUpRequest(
        @NotBlank(message = "이름을 입력해 주세요.")
        @Length(max = 255, message = "적절한 이름을 입력해 주세요 (최대 255자)")
        String name,
        @NotBlank(message = "이메일을 입력해 주세요.")
        @Length(max = 255, message = "적절한 이메일을 입력해 주세요 (최대 255자)")
        String email,
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        @Length(max = 255, message = "적절한 비밀번호를 입력해 주세요 (최대 255자)")
        String password,
        @NotBlank(message = "핸드폰번호를 입력해 주세요.")
        @Length(max = 11, message = "적절한 핸드폰번호를 입력해 주세요 (최대 11자)")
        String phoneNumber,
        @NotBlank(message = "사용자명을 입력해 주세요.")
        @Length(max = 10, message = "적절한 사용자명을 입력해 주세요 (최대 10자)")
        String nickname,
        String profileImageUrl
) {
    public User toEntity(String hashedPassword) {
        return User.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(hashedPassword)
                .phoneNumber(phoneNumber)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
