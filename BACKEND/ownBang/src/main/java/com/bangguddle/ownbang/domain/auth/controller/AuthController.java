package com.bangguddle.ownbang.domain.auth.controller;

import com.bangguddle.ownbang.domain.auth.dto.UserSignUpRequest;
import com.bangguddle.ownbang.domain.auth.service.AuthService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auths")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Response<NoneResponse>> signUp(@RequestBody UserSignUpRequest request) {
        SuccessResponse<NoneResponse> response = authService.signUp(request);
        return Response.success(response);
    }
}


