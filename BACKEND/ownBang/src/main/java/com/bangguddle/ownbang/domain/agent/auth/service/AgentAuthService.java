package com.bangguddle.ownbang.domain.agent.auth.service;

import com.bangguddle.ownbang.domain.agent.auth.dto.AgentSignUpRequest;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface AgentAuthService {
    SuccessResponse<NoneResponse> signUp(Long id, AgentSignUpRequest request);
}
