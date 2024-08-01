package com.bangguddle.ownbang.domain.webrtc.service;

import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcCreateRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcRemoveUserRequest;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface WebrtcService {
    SuccessResponse getToken(WebrtcCreateRequest request);
    SuccessResponse<NoneResponse> removeToken(WebrtcRemoveUserRequest request);
}
