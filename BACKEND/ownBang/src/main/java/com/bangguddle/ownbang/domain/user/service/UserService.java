package com.bangguddle.ownbang.domain.user.service;

import com.bangguddle.ownbang.domain.mypage.dto.MyPageResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;

public interface UserService {
    SuccessResponse<MyPageResponse> myPage(Long id);
}
