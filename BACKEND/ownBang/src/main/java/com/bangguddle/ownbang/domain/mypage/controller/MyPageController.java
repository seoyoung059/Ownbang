package com.bangguddle.ownbang.domain.mypage.controller;

import com.bangguddle.ownbang.domain.mypage.dto.ModifyMyPageRequest;
import com.bangguddle.ownbang.domain.mypage.dto.MyPageResponse;
import com.bangguddle.ownbang.domain.user.service.UserService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PatchMapping("")
    public ResponseEntity<Response<NoneResponse>> modifyMyPage(
            @AuthenticationPrincipal Long id,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("data") @Valid ModifyMyPageRequest request
    ) {
        SuccessResponse<NoneResponse> response = userService.modifyMyPage(file, request, id);
        return Response.success(response);
    }
}
