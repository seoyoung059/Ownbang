package com.bangguddle.ownbang.global.config.security;

import com.bangguddle.ownbang.global.dto.Tokens;

public interface JwtProvider {
    Tokens generateTokens(Long userId);
    boolean isValid(String token, Long userId);
    Long parseUserId(String token);
}
