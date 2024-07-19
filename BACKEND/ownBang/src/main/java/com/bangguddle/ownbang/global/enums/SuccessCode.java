package com.bangguddle.ownbang.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements ResponseCode {
    // Auth API
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 성공적으로 완료되었습니다."),
    //Reservation API
    RESERVATION_MAKE_SUCCESS(HttpStatus.CREATED, "예약신청이 성공적으로 완료되었습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
