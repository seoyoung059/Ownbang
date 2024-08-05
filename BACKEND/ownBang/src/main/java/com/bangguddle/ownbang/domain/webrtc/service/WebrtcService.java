package com.bangguddle.ownbang.domain.webrtc.service;

import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcCreateTokenRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcRemoveTokenRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcTokenResponse;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface WebrtcService {
    SuccessResponse<WebrtcTokenResponse> getToken(WebrtcCreateTokenRequest request, Long userId);
    SuccessResponse<NoneResponse> removeToken(WebrtcRemoveTokenRequest request, Long userId);
}
