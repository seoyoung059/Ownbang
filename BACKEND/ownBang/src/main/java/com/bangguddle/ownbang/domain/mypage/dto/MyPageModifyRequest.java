package com.bangguddle.ownbang.domain.mypage.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record MyPageModifyRequest(
        @NotBlank(message = "닉네임을 입력해주세요.")
        @Length(max = 10, message = "적절한 길이의 닉네임을 입력해 주세요. (최대 10자)")
        String nickname) { }
