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

    @Override
    public SuccessResponse<NoneResponse> registerChecklistTemplate(Long userId, ChecklistTemplateCreateRequest request) {
        // userid 유효성 검사
        User user = userRepository.getById(userId);
        
        // title 중복 검사
        if(checklistRepository.existsChecklistByUserIdAndTitleAndIsTemplate(userId, request.title(), true)){
            throw new AppException(CHECKLIST_DUPLICATED);
        }

        // 저장
        checklistRepository.save(request.toEntity(user));

        return new SuccessResponse<>(CHECKLIST_TEMPLATE_CREATE_SUCCESS ,NoneResponse.NONE);
    }

    @Override
    public SuccessResponse<NoneResponse> registerChecklist(Long userId, ChecklistCreateRequest request) {
        // userid 유효성 검사
        User user = userRepository.getById(userId);

        // roomId 유효성 검사
        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new AppException(BAD_REQUEST));

        // room & user 로 검색 및 업데이트
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

    @Override
    public SuccessResponse<ChecklistSearchResponse> getChecklist(Long userId, Long checklistId) {
        // userid 유효성 검사
        userRepository.getById(userId);

        // checklist 조회
        Checklist checklist = checklistRepository.findChecklistByIdAndUserId(checklistId, userId)
                .orElseThrow(() -> new AppException(BAD_REQUEST));

        // 반환
        ChecklistSearchResponse response = ChecklistSearchResponse.from(checklist);
        return new SuccessResponse<>(CHECKLIST_FIND_SUCCESS, response);
    }

    @Override
    public SuccessResponse<ChecklistSearchAllResponse> getChecklistTemplates(Long userId) {
        // userid 유효성 검사
        userRepository.getById(userId);

        // checklists 조회
        List<Checklist> checklists = checklistRepository.findByUserIdAndIsTemplateTrue(userId);

        // 반환
        ChecklistSearchAllResponse response = ChecklistSearchAllResponse.from(checklists);
        return new SuccessResponse<>(CHECKLIST_TEMPLATE_FIND_ALL_SUCCESS, response);
    }

    @Override
    public SuccessResponse<NoneResponse> modifyChecklistTemplate(Long userId, Long checklistId,
                                                                 ChecklistUpdateRequest request) {
        // userid 유효성 검사
        userRepository.getById(userId);

        // checklistId 유효성 검사
        Checklist checklist = checklistRepository.findChecklistByIdAndIsTemplate(checklistId, true)
                .orElseThrow(() -> new AppException(BAD_REQUEST));

        // title 중복 검사
        if(checklistRepository.existsChecklistByUserIdAndTitleAndIsTemplate(userId, request.title(), true)){
            throw new AppException(CHECKLIST_DUPLICATED);
        }

        // update
        checklist.update(request.title(), request.getContentsToString());
        checklistRepository.save(checklist);

        return new SuccessResponse<>(CHECKLIST_UPDATE_SUCCESS, NoneResponse.NONE);
    }

    @Override
    public SuccessResponse<NoneResponse> removeChecklistTemplate(Long userId, Long checklistId) {
        // userid 유효성 검사
        userRepository.getById(userId);

        // checklistId 유효성 검사
        Checklist checklist = checklistRepository.findChecklistByIdAndIsTemplate(checklistId, true)
                .orElseThrow(() -> new AppException(BAD_REQUEST));

        // remove
        checklistRepository.delete(checklist);

        return new SuccessResponse<>(CHECKLIST_REMOVE_SUCCESS, NoneResponse.NONE);
    }
}
