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
    BAD_CREDENTIAL(HttpStatus.UNAUTHORIZED, "이메일 또는 패스워드 오류입니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),

    // Agent Auth API
    LICENSE_NUMBER_DUPLICATED(HttpStatus.CONFLICT, "이미 가입한 자격번호입니다."),
    ALREADY_AGENT(HttpStatus.BAD_REQUEST, "이미 중개인 회원입니다"),

    // User API
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),

    // Webrtc API
    WEBRTC_SESSION_DUPLICATED(HttpStatus.CONFLICT, "이미 생성된 세션입니다"),
    WEBRTC_SESSION_CLOSED(HttpStatus.CONFLICT, "이미 종료된 세션입니다"),
    WEBRTC_SESSION_UNOPENED(HttpStatus.CONFLICT, "아직 생성되지 않은 세션입니다"),
    WEBRTC_TOKEN_DUPLICATED(HttpStatus.CONFLICT, "이미 생성된 토큰입니다."),
    WEBRTC_NO_PUBLISHER(HttpStatus.BAD_REQUEST, "해당 세션에 연결된 사용자가 없습니다."),

    // Video API
    VIDEO_DUPLICATE(HttpStatus.CONFLICT, "이미 저장된 녹화입니다."),
    VIDEO_IS_BEING_RECORDED(HttpStatus.BAD_REQUEST, "영상이 녹화 중입니다."),

    // Streaming API
    RECORDING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "녹화 영상 처리 중 문제가 발생했습니다."),
    HLS_CONVERTING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "영상 변환 중 문제가 발생했습니다."),

    // Room API
    IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패하였습니다."),
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패하였습니다."),
    INVALID_IMAGE_FILE(HttpStatus.BAD_REQUEST, "유효하지 않은 이미지 파일입니다."),
    ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "유효하지 않은 매물입니다."),

    // Bookmark API
    BOOKMARK_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 북마크입니다."),

    // AWS API
    IMAGE_S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 S3 업로드에 실패하였습니다."),
    AWS_SDK_CLIENT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AWS SDK 클라이언트 설정에 실패하였습니다."),

    // Checklist API
    CHECKLIST_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 체크리스트 템플릿입니다."),

    // Reservation API
    RESERVATION_DUPLICATED (HttpStatus.CONFLICT, " 이미 다른 사람이 신청된 예약 시간입니다. "),
    RESERVATION_COMPLETED(HttpStatus.CONFLICT, " 각 매물은 한 건씩만 예약이 가능합니다. "),
    RESERVATION_CANCELLED_DUPLICATED (HttpStatus.CONFLICT, " 이미 취소한 예약입니다. "),
    RESERVATION_CANCELLED_UNAVAILABLE (HttpStatus.BAD_REQUEST, " 확정된 예약은 취소할 수 없습니다."),
    RESERVATION_CONFIRMED_DUPLICATED(HttpStatus.CONFLICT, " 이미 확정된 예약 입니다. "),
    RESERVATION_CONFIRMED_UNAVAILABLE(HttpStatus.BAD_REQUEST, " 취소된 예약은 확정할 수 없습니다. "),
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "유효하지 않은 예약입니다."),
    RESERVATION_STATUS_NOT_CONFIRMED(HttpStatus.BAD_REQUEST, "확정된 예약이 아닙니다."),
    RESERVATION_CONFIRMED_DUPLICATED_TIME_ROOM(HttpStatus.CONFLICT, "예약 확정은 각 매물, 시간 당 한 건만 가능합니다."),
    WORKHOUR_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 날짜의 근무 시간 정보를 찾을 수 없습니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "유효하지 않은 날짜 형식입니다."),

    // Common Error Code
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부적 에러가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 요청을 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메소드 입니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없는 요청입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
