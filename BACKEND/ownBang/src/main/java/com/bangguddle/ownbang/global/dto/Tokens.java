package com.bangguddle.ownbang.global.dto;

import lombok.Builder;

@Builder
public record Tokens(String accessToken, String refreshToken) { }