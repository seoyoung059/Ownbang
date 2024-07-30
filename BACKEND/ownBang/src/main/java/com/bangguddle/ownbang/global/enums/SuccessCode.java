package com.bangguddle.ownbang.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements ResponseCode {
    // Auth API
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 성공적으로 완료되었습니다."),
    CHECK_EMAIL_DUPLICATE_SUCCESS(HttpStatus.OK, "이메일 중복 조회가 성공적으로 완료되었습니다."),
    CHECK_PHONE_NUMBER_DUPLICATE_SUCCESS(HttpStatus.OK, "전화번호 중복 조회가 성공적으로 완료되었습니다."),

    // Room API
    ROOM_REGISTER_SUCCESS(HttpStatus.CREATED, "매물이 성공적으로 등록되었습니다."),
    ROOM_UPDATE_SUCCESS(HttpStatus.OK, "매물 정보가 성공적으로 수정되었습니다."),
    ROOM_DELETE_SUCCESS(HttpStatus.OK, "매물이 성공적으로 삭제되었습니다."),

    ROOM_FIND_SUCCESS(HttpStatus.OK, "해당하는 ID의 매물을 찾았습니다."),



    ROOM_IMAGE_UPLOAD_SUCCESS(HttpStatus.OK, "사진이 성공적으로 저장되었습니다."),
    ROOM_IMAGE_DELETE_SUCCESS(HttpStatus.OK, "사진이 성공적으로 삭제되었습니다."),




    //Reservation API
    RESERVATION_MAKE_SUCCESS(HttpStatus.CREATED, "예약 신청이 성공적으로 완료되었습니다."),
    RESERVATION_UPDATE_STATUS_SUCCESS(HttpStatus.OK, "예약 철회가 성공적으로 완료되었습니다."),
    RESERVATION_CONFIRM_SUCCESS(HttpStatus.CREATED, "예약 확정이 성공적으로 완료되었습니다."),
    
    RESERVATION_LIST_SUCCESS(HttpStatus.OK ,"내가 신청한 예약 목록을 조회할 수 있습니다."),

    RESERVATION_LIST_EMPTY(HttpStatus.OK, " 예약한 목록이 없습니다. ");
    private final HttpStatus httpStatus;
    private final String message;
}
