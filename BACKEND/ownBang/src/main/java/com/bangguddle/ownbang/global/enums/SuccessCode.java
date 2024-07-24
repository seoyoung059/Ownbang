package com.bangguddle.ownbang.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements ResponseCode {
    // Auth API
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 성공적으로 완료되었습니다."),



    // Room API
    ROOM_REGISTER_SUCCESS(HttpStatus.CREATED, "매물이 성공적으로 등록되었습니다."),
    ROOM_UPDATE_SUCCESS(HttpStatus.OK, "매물 정보가 성공적으로 수정되었습니다."),
    ROOM_DELETE_SUCCESS(HttpStatus.OK, "매물이 성공적으로 삭제되었습니다."),



    ROOM_IMAGE_UPLOAD_SUCCESS(HttpStatus.OK, "사진이 성공적으로 저장되었습니다.");




    private final HttpStatus httpStatus;
    private final String message;
}
