package com.bangguddle.ownbang.global.response;

import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record Response<T>(String resultCode, String code, String message, T data) {
    public static <T> ResponseEntity<Response<T>> success(SuccessCode code, T data) {
        return new ResponseEntity<>(new Response<>("SUCCESS", code.name(),
                code.getMessage(), data), HttpStatus.OK);
    }

    public static <T> Response<T> error(ErrorCode code, T data) {
        return new Response<>("ERROR", code.name(), code.getMessage(), data);
    }
}
