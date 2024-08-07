package com.bangguddle.ownbang.domain.agent.workhour.controller;

import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourListResponse;
import com.bangguddle.ownbang.domain.agent.workhour.dto.AgentWorkhourRequest;
import com.bangguddle.ownbang.domain.agent.workhour.service.AgentWorkhourService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("agents/workhour")
@RequiredArgsConstructor
public class AgentWorkhourController {

    private final AgentWorkhourService agentWorkhourService;

    @PostMapping
    public ResponseEntity<Response<NoneResponse>> createAgentWorkhour(@AuthenticationPrincipal Long userId, @RequestBody @Valid AgentWorkhourRequest request) {
        SuccessResponse<NoneResponse> response = agentWorkhourService.createAgentWorkhour(userId, request);
        return Response.success(response);
    }

    @GetMapping
    public ResponseEntity<Response<AgentWorkhourListResponse>> getAgentWorkhour(
            @RequestParam(name="agentId") Long agentId) {
        SuccessResponse<AgentWorkhourListResponse> response = agentWorkhourService.getAgentWorkhour(agentId);
        return Response.success(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response<NoneResponse>> updateAgentWorkhour(
            @PathVariable("id") Long id,@AuthenticationPrincipal Long userId,
            @RequestBody @Valid AgentWorkhourRequest request) {
        SuccessResponse<NoneResponse> response = agentWorkhourService.updateAgentWorkhour(id, userId, request);
        return Response.success(response);
    }
}
