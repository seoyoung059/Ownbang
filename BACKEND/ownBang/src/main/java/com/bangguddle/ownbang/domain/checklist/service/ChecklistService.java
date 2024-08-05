package com.bangguddle.ownbang.domain.checklist.service;

import com.bangguddle.ownbang.domain.checklist.dto.ChecklistSearchResponse;
import com.bangguddle.ownbang.domain.checklist.dto.ChecklistTemplateCreateRequest;
import com.bangguddle.ownbang.domain.checklist.dto.ChecklistUpdateRequest;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface ChecklistService {
    SuccessResponse<NoneResponse> registerChecklistTemplate(Long userId, ChecklistTemplateCreateRequest request);
    SuccessResponse<ChecklistSearchResponse> getChecklist(Long userId, Long checklistId);
    SuccessResponse<NoneResponse> modifyChecklistTemplate(Long userId, Long checklistId,
                                                          ChecklistUpdateRequest request);
}
