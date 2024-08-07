package com.bangguddle.ownbang.domain.agent.auth.controller;

import com.bangguddle.ownbang.domain.agent.auth.dto.AgentWorkhourListResponse;
import com.bangguddle.ownbang.domain.agent.auth.dto.AgentWorkhourRequest;
import com.bangguddle.ownbang.domain.agent.auth.service.AgentWorkhourService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("agents/workhour")
@RequiredArgsConstructor
public class AgentWorkhourController {

    private final AgentWorkhourService agentWorkhourService;

    @PostMapping
    public ResponseEntity<Response<NoneResponse>> createAgentWorkhour(@RequestBody @Valid AgentWorkhourRequest request) {
        SuccessResponse<NoneResponse> response =  agentWorkhourService.createAgentWorkhour(request);
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
            @PathVariable("id") Long id,
            @RequestBody @Valid AgentWorkhourRequest request) {
        SuccessResponse<NoneResponse> response = agentWorkhourService.updateAgentWorkhour(id, request);
        return Response.success(response);
    }
}
