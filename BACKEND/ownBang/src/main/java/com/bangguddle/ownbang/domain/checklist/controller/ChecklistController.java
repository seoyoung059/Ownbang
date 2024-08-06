package com.bangguddle.ownbang.domain.checklist.controller;

import com.bangguddle.ownbang.domain.checklist.dto.*;
import com.bangguddle.ownbang.domain.checklist.service.ChecklistService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checklists")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @PostMapping("/template")
    public ResponseEntity<Response<NoneResponse>> registerChecklistTemplate(
            @AuthenticationPrincipal Long userId,
            @RequestBody ChecklistTemplateCreateRequest request) {
        SuccessResponse<NoneResponse> response = checklistService.registerChecklistTemplate(userId, request);
        return Response.success(response);
    }

    @PostMapping("/non-template")
    public ResponseEntity<Response<NoneResponse>> registerChecklist(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ChecklistCreateRequest request) {
        SuccessResponse<NoneResponse> response = checklistService.registerChecklist(userId, request);
        return Response.success(response);
    }

    @GetMapping("/{checklistId}")
    public ResponseEntity<Response<ChecklistSearchResponse>> getChecklist(
            @AuthenticationPrincipal Long userId,
            @PathVariable @Positive @Valid Long checklistId) {
        SuccessResponse<ChecklistSearchResponse> response = checklistService.getChecklist(userId, checklistId);
        return Response.success(response);
    }

    @GetMapping
    public ResponseEntity<Response<ChecklistSearchAllResponse>> getChecklistTemplates(
            @AuthenticationPrincipal Long userId) {
        SuccessResponse<ChecklistSearchAllResponse> response = checklistService.getChecklistTemplates(userId);
        return Response.success(response);
    }

    @PatchMapping("/{checklistId}")
    public ResponseEntity<Response<NoneResponse>> modifyChecklistTemplate(
            @AuthenticationPrincipal Long userId,
            @PathVariable @Positive @Valid Long checklistId,
            @RequestBody ChecklistUpdateRequest request) {
        SuccessResponse<NoneResponse> response = checklistService.modifyChecklistTemplate(userId, checklistId, request);
        return Response.success(response);
    }

    @DeleteMapping("/{checklistId}")
    public ResponseEntity<Response<NoneResponse>> removeChecklistTemplate(
            @AuthenticationPrincipal Long userId,
            @PathVariable @Positive @Valid Long checklistId) {
        SuccessResponse<NoneResponse> response = checklistService.removeChecklistTemplate(userId, checklistId);
        return Response.success(response);
    }
}
