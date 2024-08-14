package com.bangguddle.ownbang.domain.checklist.service;

import com.bangguddle.ownbang.domain.checklist.dto.*;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface ChecklistService {
    SuccessResponse<NoneResponse> registerChecklistTemplate(Long userId, ChecklistTemplateCreateRequest request);
    SuccessResponse<NoneResponse> registerChecklist(Long userId, ChecklistCreateRequest request);
    SuccessResponse<ChecklistSearchResponse> getChecklist(Long userId, Long checklistId);
    SuccessResponse<ChecklistSearchResponse> getChecklistByReservationId(Long userId, Long reservationId);
    SuccessResponse<ChecklistSearchAllResponse> getChecklistTemplates(Long userId);
    SuccessResponse<NoneResponse> modifyChecklistTemplate(Long userId, Long checklistId,
                                                          ChecklistUpdateRequest request);
    SuccessResponse<NoneResponse> modifyChecklistByReservationId(Long userId, Long reservationId,
                                                                 ChecklistUpdateRequest request);
    SuccessResponse<NoneResponse> removeChecklistTemplate(Long userId, Long checklistId);
}
