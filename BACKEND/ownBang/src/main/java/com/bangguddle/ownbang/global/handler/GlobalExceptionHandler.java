package com.bangguddle.ownbang.global.handler;

import com.bangguddle.ownbang.global.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> AppExceptionHandler(AppException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(Response.error(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> commonExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(e));
    }
}
