package com.bangguddle.ownbang.domain.agent.service;

import com.bangguddle.ownbang.domain.agent.mypage.dto.AgentMyPageModifyRequest;
import com.bangguddle.ownbang.domain.agent.mypage.dto.AgentMyPageResponse;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface AgentService {
    SuccessResponse<AgentMyPageResponse> getMyPage(Long id);
    SuccessResponse<NoneResponse> modifyMyPage(Long id, AgentMyPageModifyRequest request);
}
