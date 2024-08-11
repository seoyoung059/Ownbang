package com.bangguddle.ownbang.domain.mypage.dto;

import jakarta.validation.constraints.NotBlank;

public record MyPageModifyRequest(@NotBlank String nickname) { }
