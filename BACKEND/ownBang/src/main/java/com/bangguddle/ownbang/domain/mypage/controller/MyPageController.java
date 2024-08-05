package com.bangguddle.ownbang.domain.mypage.controller;

import com.bangguddle.ownbang.domain.mypage.dto.MyPageResponse;
import com.bangguddle.ownbang.domain.user.service.UserService;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final UserService userService;
    @GetMapping("")
    public ResponseEntity<Response<MyPageResponse>> getMyPage(@AuthenticationPrincipal Long id) {
        SuccessResponse<MyPageResponse> response = userService.getMyPage(id);
        return Response.success(response);
    }
}
