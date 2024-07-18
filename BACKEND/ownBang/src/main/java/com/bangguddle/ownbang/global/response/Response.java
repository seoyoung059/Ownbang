package com.bangguddle.ownbang.global.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record Response<T>(String resultCode, T result) {
    public static <T> ResponseEntity<Response<T>> created(T result) {
        return new ResponseEntity<>(new Response<>("SUCCESS", result), HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<Response<T>> success(T result) {
        return new ResponseEntity<>(new Response<>("SUCCESS", result), HttpStatus.OK);
    }

    public static <T> Response<T> error(T result) {
        return new Response<>("ERROR", result);
    }

}
