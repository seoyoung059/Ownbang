package com.bangguddle.ownbang.domain.streaming.controller;

import com.bangguddle.ownbang.domain.streaming.dto.StreamingConvertRequest;
import com.bangguddle.ownbang.domain.streaming.service.StreamingService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/streamings")
@RequiredArgsConstructor
public class StreamController {

    private final StreamingService streamingService;

    @GetMapping
    public ResponseEntity<Response<NoneResponse>> getStreamingConvert(@RequestBody StreamingConvertRequest request) {
        streamingService.uploadStreaming(request.reservationId(), request.sessionId());
        return Response.success(new SuccessResponse<NoneResponse>(SuccessCode.BOOKMARK_CREATE_SUCCESS, NoneResponse.NONE));
    }

}