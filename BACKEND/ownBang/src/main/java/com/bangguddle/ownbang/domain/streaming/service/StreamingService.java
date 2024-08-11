package com.bangguddle.ownbang.domain.streaming.service;

import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface StreamingService {
    // zip파일 위치, 중개인 토큰명, reservationId 받아 압축 해제, hls 변환, s3 업로드 하고, 완료 후 m3u8의 uri를 return하는 부분
    SuccessResponse<String> uploadStreaming(String sessionId);
}
