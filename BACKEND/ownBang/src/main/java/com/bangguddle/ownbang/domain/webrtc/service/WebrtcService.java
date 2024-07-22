package com.bangguddle.ownbang.domain.webrtc.service;

import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface WebrtcService {
    SuccessResponse getToken(String sessionName);
}
