package com.bangguddle.ownbang.domain.auth.dto;

import lombok.Builder;

@Builder
public record LoginRequest(String email, String password) { }
