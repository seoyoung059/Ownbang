package com.bangguddle.ownbang.domain.asks.service;

import com.bangguddle.ownbang.domain.asks.dto.AskContentCreateRequest;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface AskService {
    SuccessResponse<NoneResponse> createAsk(Long userId, Long roomId);

    SuccessResponse<NoneResponse> createAskContent(Long userId, AskContentCreateRequest request);
}
