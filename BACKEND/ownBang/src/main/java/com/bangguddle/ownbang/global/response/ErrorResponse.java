package com.bangguddle.ownbang.global.response;

import com.bangguddle.ownbang.global.enums.ErrorCode;

public record ErrorResponse(ErrorCode code, String message) { }
