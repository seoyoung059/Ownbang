package com.bangguddle.ownbang.domain.checklist.service.impl;

import com.bangguddle.ownbang.domain.checklist.dto.ChecklistSearchResponse;
import com.bangguddle.ownbang.domain.checklist.dto.ChecklistTemplateCreateRequest;
import com.bangguddle.ownbang.domain.checklist.entity.Checklist;
import com.bangguddle.ownbang.domain.checklist.repository.ChecklistRepository;
import com.bangguddle.ownbang.domain.checklist.service.ChecklistService;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;

@Service
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final UserRepository userRepository;

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
}
