package com.bangguddle.ownbang.domain.checklist.service.impl;

import com.bangguddle.ownbang.domain.checklist.dto.*;
import com.bangguddle.ownbang.domain.checklist.entity.Checklist;
import com.bangguddle.ownbang.domain.checklist.repository.ChecklistRepository;
import com.bangguddle.ownbang.domain.checklist.service.ChecklistService;
import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;

@Service
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 체크리스트 템플릿을 저장합니다.<br/>
     * 동일한 제목의 템플릿은 저장되지 않습니다.
     * @param userId
     * @param request 제목, 항목
     * @return NoneResponse
     */
    @Override
    public SuccessResponse<NoneResponse> registerChecklistTemplate(final Long userId,
                                                                   final ChecklistTemplateCreateRequest request) {
        // userid 유효성 검사
        User user = userRepository.getById(userId);

        // title 중복 검사 - user & title 이용
        validateTemplateByUserAndTitle(userId, request.title());

        // 저장
        checklistRepository.save(request.toEntity(user));

        return new SuccessResponse<>(CHECKLIST_TEMPLATE_CREATE_SUCCESS ,NoneResponse.NONE);
    }


    /**
     * 체크리스트 객체를 저장합니다.<br/>
     * 동일한 예약건에 대한 저장은 제목과 항목이 덮어쓰기 됩니다.<br/>
     * 화상 통화에서 객체 임시 및 종료시에 사용됩니다.
     * @param userId
     * @param request 예약 아이디, 제목, 항목
     * @return NoneResponse
     */
    @Override
    public SuccessResponse<NoneResponse> registerChecklist(final Long userId,
                                                           final ChecklistCreateRequest request) {
        // userid 유효성 검사
        User user = userRepository.getById(userId);

        // roomId 유효성 검사
        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new AppException(BAD_REQUEST));

        // checklist 조회 및 업데이트 - room & user 이용
        Checklist checklist = checklistRepository.findByUserAndReservation(user, reservation)
                .map(existingChecklist -> {
                    existingChecklist.update(request.title(), request.contentsToString());
                    return existingChecklist;
                })
                .orElseGet(() -> request.toEntity(user, reservation));

        // 저장
        checklistRepository.save(checklist);

        return new SuccessResponse<>(CHECKLIST_CREATE_SUCCESS ,NoneResponse.NONE);
    }


    /**
     * checklistId에 해당하는 체크리스트 템플릿 또는 객체를 반환합니다.
     * @param userId
     * @param checklistId
     * @return ChecklistSearchResponse
     */
    @Override
    public SuccessResponse<ChecklistSearchResponse> getChecklist(final Long userId,
                                                                 final Long checklistId) {
        // userid 유효성 검사
        userRepository.getById(userId);

        // checklist 조회 - checklist & user 이용
        Checklist checklist = checklistRepository.findByIdAndUserId(checklistId, userId)
                .orElseThrow(() -> new AppException(BAD_REQUEST));

        // 반환
        ChecklistSearchResponse response = ChecklistSearchResponse.from(checklist);
        return new SuccessResponse<>(CHECKLIST_FIND_SUCCESS, response);
    }


    /**
     * 체크리스트 템플릿 목록을 반환합니다.
     * @param userId
     * @return ChecklistSearchAllResponse
     */
    @Override
    public SuccessResponse<ChecklistSearchAllResponse> getChecklistTemplates(final Long userId) {
        // userid 유효성 검사
        userRepository.getById(userId);

        // checklists 조회 - user 이용
        List<Checklist> checklists = checklistRepository.findByUserIdAndIsTemplateTrue(userId);

        // 반환
        ChecklistSearchAllResponse response = ChecklistSearchAllResponse.from(checklists);
        return new SuccessResponse<>(CHECKLIST_TEMPLATE_FIND_ALL_SUCCESS, response);
    }


    /**
     * checklistId에 해당하는 체크리스트 템플릿을 수정합니다. <br/>
     * 제목과 항목들이 덮어쓰기 됩니다.
     * @param userId
     * @param checklistId
     * @param request 제목, 항목
    public SuccessResponse<NoneResponse> modifyChecklistTemplate(final Long userId,
     * @return
     */
    @Override
    public SuccessResponse<NoneResponse> modifyChecklistTemplate(final Long userId,
                                                                 final Long checklistId,
                                                                 final ChecklistUpdateRequest request) {
        // userid 유효성 검사
        userRepository.getById(userId);

        // title 중복 검사 - user & title 이용
        validateTemplateByUserAndTitle(userId, request.title());

        // checklistId 유효성 검사 - checklist & user 이용
        Checklist checklist = validateTemplateByIdAndUser(checklistId, userId);

        // update
        checklist.update(request.title(), request.getContentsToString());
        checklistRepository.save(checklist);

        return new SuccessResponse<>(CHECKLIST_UPDATE_SUCCESS, NoneResponse.NONE);
    }


    /**
     * checklistId에 해당하는 체크리스트 템플릿을 삭제합니다.
     * @param userId
     * @param checklistId
     * @return NoneResponse
     */
    @Override
    public SuccessResponse<NoneResponse> removeChecklistTemplate(final Long userId,
                                                                 final Long checklistId) {
        // userid 유효성 검사
        userRepository.getById(userId);

        // checklistId 유효성 검사 - checklist & user 이용
        Checklist checklist = validateTemplateByIdAndUser(checklistId, userId);

        // remove
        checklistRepository.delete(checklist);

        return new SuccessResponse<>(CHECKLIST_REMOVE_SUCCESS, NoneResponse.NONE);
    }

    // title 중복 검사 - user & title 이용
    private void validateTemplateByUserAndTitle(final Long userId, final String title){
        if(checklistRepository.existsByUserIdAndTitleAndIsTemplateTrue(userId, title)){
            throw new AppException(CHECKLIST_DUPLICATED);
        }
    }

    // checklistId 유효성 검사 - checklist & user 이용
    private Checklist validateTemplateByIdAndUser(final Long checklistId, final Long userId){
        return checklistRepository.findByIdAndUserIdAndIsTemplateTrue(checklistId, userId)
                .orElseThrow(() -> new AppException(BAD_REQUEST));
    }
}
