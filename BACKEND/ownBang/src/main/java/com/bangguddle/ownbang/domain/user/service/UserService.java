package com.bangguddle.ownbang.domain.user.service;

import com.bangguddle.ownbang.domain.mypage.dto.ModifyMyPageRequest;
import com.bangguddle.ownbang.domain.mypage.dto.MyPageResponse;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    SuccessResponse<MyPageResponse> getMyPage(Long id);
    SuccessResponse<NoneResponse> modifyMyPage(MultipartFile file, ModifyMyPageRequest request, Long id);
}
