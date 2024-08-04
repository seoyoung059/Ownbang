package com.bangguddle.ownbang.domain.asks.controller;


import com.bangguddle.ownbang.domain.asks.dto.AskContentCreateRequest;
import com.bangguddle.ownbang.domain.asks.service.AskService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("asks")
@RequiredArgsConstructor
public class AskController {

    private final AskService askService;

    @PostMapping("/{roomId}")
    public ResponseEntity<Response<NoneResponse>> createAgentAsk(@AuthenticationPrincipal Long userId,
                                                                   @PathVariable Long roomId) {
        return Response.success(askService.createAsk(userId, roomId));
    }

    @PostMapping
    public ResponseEntity<Response<NoneResponse>> createAskContent(@AuthenticationPrincipal Long userId,
                                                                          @RequestBody AskContentCreateRequest request) {
        return Response.success(askService.createAskContent(userId, request));
    }


//    @GetMapping
//    public ResponseEntity<Response<List<AskSearchResponse>>> getUserAsks(@AuthenticationPrincipal Long userId) {
//
//    }

//    @GetMapping("/agent")
//    public ResponseEntity<Response<List<AskSearchResponse>>> getAgentAsks(@AuthenticationPrincipal Long userId) {
//
//    }
}
