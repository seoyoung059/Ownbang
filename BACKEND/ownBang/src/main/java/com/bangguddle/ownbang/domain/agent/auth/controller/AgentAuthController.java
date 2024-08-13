package com.bangguddle.ownbang.domain.agent.auth.controller;

import com.bangguddle.ownbang.domain.agent.auth.dto.AgentSignUpRequest;
import com.bangguddle.ownbang.domain.agent.auth.service.AgentAuthService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("agents/auths")
@RequiredArgsConstructor
public class AgentAuthController {
    private final AgentAuthService agentAuthService;

    @PostMapping("/sign-up")
    public ResponseEntity<Response<NoneResponse>> signUp(
            @AuthenticationPrincipal Long id,
            @RequestBody @Valid AgentSignUpRequest request) {
        SuccessResponse<NoneResponse> response = agentAuthService.signUp(id, request);
        return Response.success(response);
    }
}
