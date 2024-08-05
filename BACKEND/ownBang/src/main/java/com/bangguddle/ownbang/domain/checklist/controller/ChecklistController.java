package com.bangguddle.ownbang.domain.checklist.controller;

import com.bangguddle.ownbang.domain.checklist.dto.ChecklistTemplateCreateRequest;
import com.bangguddle.ownbang.domain.checklist.service.ChecklistService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
