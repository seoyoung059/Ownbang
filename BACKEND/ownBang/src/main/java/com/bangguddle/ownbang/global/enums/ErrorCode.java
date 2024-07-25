package com.bangguddle.ownbang.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements ResponseCode {
    // Auth API
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 가입한 이메일입니다."),
    PHONE_NUMBER_DUPLICATED(HttpStatus.CONFLICT, "이미 가입한 전화번호 입니다."),

    // Room API
    INVALID_IMAGE_FILE(HttpStatus.BAD_REQUEST, "유효하지 않은 이미지 파일입니다."),
    
    // Reservation API
    RESERVATION_DUPLICATED (HttpStatus.CONFLICT, " 이미 다른 사람이 신청된 예약 시간입니다. "),
    RESERVATION_COMPLETED(HttpStatus.CONFLICT, " 각 매물은 한 건씩만 예약이 가능합니다. "),
    RESERVATION_CANCELED_DUPLICATED (HttpStatus.CONFLICT, " 이미 취소한 예약입니다. "),
    RESERVATION_CANCELED_UNAVAILABLE (HttpStatus.BAD_REQUEST, " 확정된 예약은 취소할 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "유효하지 않은 예약입니다."),

    // Common Error Code
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부적 에러가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 요청을 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메소드 입니다");


    private final HttpStatus httpStatus;
    private final String message;
}
