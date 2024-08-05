package com.bangguddle.ownbang.domain.user.service.impl;

import com.bangguddle.ownbang.domain.mypage.dto.ModifyMyPageRequest;
import com.bangguddle.ownbang.domain.mypage.dto.MyPageResponse;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.domain.user.service.UserService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.bangguddle.ownbang.global.enums.SuccessCode.GET_MY_PAGE_SUCCESS;
import static com.bangguddle.ownbang.global.enums.SuccessCode.UPDATE_MY_PAGE_SUCCESS;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public SuccessResponse<MyPageResponse> getMyPage(Long id) {
        User user = userRepository.getById(id);
        MyPageResponse response = MyPageResponse.from(user);
        return new SuccessResponse<>(GET_MY_PAGE_SUCCESS, response);
    }

    // s3 관련 추후 수정
    @Override
    public SuccessResponse<NoneResponse> modifyMyPage(MultipartFile file, ModifyMyPageRequest request, Long id) {
        User user = userRepository.getById(id);
//        if (file != null)
//            user.updateUserProfile(s3FileUploadService.uploadFile(file), request.nickname());
        return new SuccessResponse<>(UPDATE_MY_PAGE_SUCCESS, NoneResponse.NONE);
    }

}
