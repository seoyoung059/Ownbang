package com.bangguddle.ownbang.global.response;

import com.bangguddle.ownbang.global.enums.SuccessCode;

public record SuccessResponse<T>(SuccessCode successCode, T data) {
}
