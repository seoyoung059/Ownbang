package com.bangguddle.ownbang.domain.agent.auth.controller;

import com.bangguddle.ownbang.domain.agent.auth.dto.AgentWorkhourListResponse;
import com.bangguddle.ownbang.domain.agent.auth.service.AgentWorkhourService;
import com.bangguddle.ownbang.domain.agent.entity.AgentWorkhour;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("agents/workhour")
@RequiredArgsConstructor
public class AgentWorkhourController {

    private final AgentWorkhourService agentWorkhourService;

    @PostMapping
    public ResponseEntity<Response<NoneResponse>> createAgentWorkhour(@RequestBody AgentWorkhour agentWorkhour) {
        SuccessResponse<NoneResponse> response = agentWorkhourService.createAgentWorkhour(agentWorkhour);
        return Response.success(response);
    }

    @GetMapping
    public ResponseEntity<Response<AgentWorkhourListResponse>> getAgentWorkhour(
            @RequestParam Long agentId) {
        SuccessResponse<AgentWorkhourListResponse> response = agentWorkhourService.getAgentWorkhour(agentId);
        return Response.success(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response<NoneResponse>> updateAgentWorkhour(
            @PathVariable Long id,
            @RequestBody AgentWorkhour agentWorkhour) {
        SuccessResponse<NoneResponse> response = agentWorkhourService.updateAgentWorkhour(id, agentWorkhour);
        return Response.success(response);
    }
}
