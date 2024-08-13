package com.bangguddle.ownbang.domain.agent.mypage.controller;

import com.bangguddle.ownbang.domain.agent.mypage.dto.AgentMyPageModifyRequest;
import com.bangguddle.ownbang.domain.agent.mypage.dto.AgentMyPageResponse;
import com.bangguddle.ownbang.domain.agent.service.AgentService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("agents/mypage")
@RequiredArgsConstructor
public class AgentMyPageController {
    private final AgentService agentService;
    @GetMapping("")
    public ResponseEntity<Response<AgentMyPageResponse>> getMyPage(@AuthenticationPrincipal Long id) {
        SuccessResponse<AgentMyPageResponse> response = agentService.getMyPage(id);
        return Response.success(response);
    }

    @PatchMapping("")
    public ResponseEntity<Response<NoneResponse>> modifyMyPage(
            @AuthenticationPrincipal Long id,
            @RequestBody @Valid AgentMyPageModifyRequest request) {
        SuccessResponse<NoneResponse> response = agentService.modifyMyPage(id, request);
        return Response.success(response);
    }
}
