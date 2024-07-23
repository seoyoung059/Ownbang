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
    RESERVATION_MAKE_SUCCESS(HttpStatus.CREATED, "예약 신청이 성공적으로 완료되었습니다."),
    RESERVATION_CONFIRM_SUCCESS(HttpStatus.CREATED, "예약 확정이 성공적으로 완료되었습니다."),
    RESERVATION_LIST_SUCCESS(HttpStatus.OK ,"내가 신청한 예약 목록을 조회할 수 있습니다."),
    RESERVATION_LIST_EMPTY(HttpStatus.OK, " 예약한 목록이 없습니다. ");
    private final HttpStatus httpStatus;
    private final String message;
}
