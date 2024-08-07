package com.bangguddle.ownbang.domain.user.service.impl;

import com.bangguddle.ownbang.domain.mypage.dto.MyPageModifyRequest;
import com.bangguddle.ownbang.domain.mypage.dto.MyPageResponse;
import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.domain.user.service.UserService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import com.bangguddle.ownbang.global.service.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.bangguddle.ownbang.global.enums.SuccessCode.GET_MY_PAGE_SUCCESS;
import static com.bangguddle.ownbang.global.enums.SuccessCode.UPDATE_MY_PAGE_SUCCESS;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final S3UploaderService s3UploaderService;

    @Value("${S3_USERIMG_PATH}")
    private String userImagePath;


    @Override
    public SuccessResponse<MyPageResponse> getMyPage(Long id) {
        User user = userRepository.getById(id);
        MyPageResponse response = MyPageResponse.from(user);
        return new SuccessResponse<>(GET_MY_PAGE_SUCCESS, response);
    }

    @Override
    public SuccessResponse<NoneResponse> modifyMyPage(MultipartFile file, MyPageModifyRequest request, Long id) {
        User user = userRepository.getById(id);
        String uploadedFileUrl = user.getProfileImageUrl();
        if (file != null) {
            uploadedFileUrl = s3UploaderService.uploadFile(file, userImagePath);
        }
        user.updateUserProfile(uploadedFileUrl, request.nickname());
        userRepository.save(user);
        return new SuccessResponse<>(UPDATE_MY_PAGE_SUCCESS, NoneResponse.NONE);
    }

}
