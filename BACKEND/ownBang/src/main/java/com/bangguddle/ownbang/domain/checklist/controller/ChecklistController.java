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

    /**
     * 제목과 항목을 받아 체크리스트 템플릿을 생성합니다. <br/>
     * @param userId
     * @param request 제목, 항목
     * @return NoneResponse
     */
    @PostMapping("/template")
    public ResponseEntity<Response<NoneResponse>> registerChecklistTemplate(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ChecklistTemplateCreateRequest request) {
        SuccessResponse<NoneResponse> response = checklistService.registerChecklistTemplate(userId, request);
        return Response.success(response);
    }


    /**
     * 예약 번호, 제목, 항목을 받아 체크리스트 객체를 생성합니다.<br/>
     * 동일한 예약 번호라면, 제목과 항목이 덮어쓰기 됩니다.
     * @param userId
     * @param request 예약번호, 제목, 항목
     * @return NoneResponse
     */
    @PostMapping("/non-template")
    public ResponseEntity<Response<NoneResponse>> registerChecklist(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid ChecklistCreateRequest request) {
        SuccessResponse<NoneResponse> response = checklistService.registerChecklist(userId, request);
        return Response.success(response);
    }


//    /**
//     * checklistId와 동일한 체크리스트를 반환합니다.<br/>
//     * 체크리스트 템플릿 또는 객체를 반환할 수 있습니다.<br/>
//     * 마이페이지에서 템플릿 수정 또는 다시보기에서 객체 확인용으로 사용됩니다.
//     * @param userId
//     * @param checklistId 체크리스트 아이디
//     * @return ChecklistSearchResponse
//     */
//    @GetMapping("/{checklistId}")
//    public ResponseEntity<Response<ChecklistSearchResponse>> getChecklist(
//            @AuthenticationPrincipal Long userId,
//            @PathVariable @Positive @Valid Long checklistId) {
//        SuccessResponse<ChecklistSearchResponse> response = checklistService.getChecklist(userId, checklistId);
//        return Response.success(response);
//    }


    /**
     * reservationId에 맞는 체크리스트를 반환합니다.<br/>
     * 체크리스트 템플릿 또는 객체를 반환할 수 있습니다.<br/>
     * 마이페이지에서 템플릿 수정 또는 다시보기에서 객체 확인용으로 사용됩니다.
     * @param userId
     * @param reservationId 체크리스트 아이디
     * @return ChecklistSearchResponse
     */
    @GetMapping("/{reservationId}")
    public ResponseEntity<Response<ChecklistSearchResponse>> getChecklistByReservationId(
            @AuthenticationPrincipal Long userId,
            @PathVariable @Positive @Valid Long reservationId) {
        SuccessResponse<ChecklistSearchResponse> response = checklistService.getChecklistByReservationId(userId, reservationId);
        return Response.success(response);
    }

    /**
     * 체크리스트 템플릿 목록을 반환합니다.<br/>
     * 마이페이지 템플릿 목록 조회에서 사용합니다.
     * @param userId
     * @return ChecklistSearchAllResponse
     */
    @GetMapping
    public ResponseEntity<Response<ChecklistSearchAllResponse>> getChecklistTemplates(
            @AuthenticationPrincipal Long userId) {
        SuccessResponse<ChecklistSearchAllResponse> response = checklistService.getChecklistTemplates(userId);
        return Response.success(response);
    }


//    /**
//     * checklistId와 동일한 체크리스트 템플릿을 수정합니다.<br/>
//     * request의 내용으로 제목과 항목을 수정합니다.
//     * @param userId
//     * @param checklistId
//     * @param request 제목, 항목
//     * @return NoneResponse
//     */
//    @PatchMapping("/{checklistId}")
//    public ResponseEntity<Response<NoneResponse>> modifyChecklistTemplate(
//            @AuthenticationPrincipal Long userId,
//            @PathVariable @Positive @Valid Long checklistId,
//            @RequestBody ChecklistUpdateRequest request) {
//        SuccessResponse<NoneResponse> response = checklistService.modifyChecklistTemplate(userId, checklistId, request);
//        return Response.success(response);
//    }


    /**
     * reservaionId에 맞는 체크리스트 템플릿을 수정합니다.<br/>
     * request의 내용으로 제목과 항목을 수정합니다.
     * @param userId
     * @param reservationId
     * @param request 제목, 항목
     * @return NoneResponse
     */
    @PatchMapping("/{reservationId}")
    public ResponseEntity<Response<NoneResponse>> modifyChecklistByReservationId(
            @AuthenticationPrincipal Long userId,
            @PathVariable @Positive @Valid Long reservationId,
            @RequestBody ChecklistUpdateRequest request) {
        SuccessResponse<NoneResponse> response = checklistService.modifyChecklistByReservationId(userId, reservationId, request);
        return Response.success(response);
    }



    /**
     * checklistId와 동일한 체크리스트 템플릿을 삭제합니다.
     * @param userId
     * @param checklistId
     * @return NoneResponse
     */
    @DeleteMapping("/{checklistId}")
    public ResponseEntity<Response<NoneResponse>> removeChecklistTemplate(
            @AuthenticationPrincipal Long userId,
            @PathVariable @Positive @Valid Long checklistId) {
        SuccessResponse<NoneResponse> response = checklistService.removeChecklistTemplate(userId, checklistId);
        return Response.success(response);
    }
}
