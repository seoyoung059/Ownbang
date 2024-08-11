package com.bangguddle.ownbang.domain.streaming.controller;

import com.bangguddle.ownbang.domain.streaming.dto.StreamingConvertRequest;
import com.bangguddle.ownbang.domain.streaming.service.impl.StreamingServiceImpl;
import com.bangguddle.ownbang.global.response.Response;
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

    private final StreamingServiceImpl streamingService;

    @GetMapping
    public ResponseEntity<Response<String>> getStreamingConvert(@RequestBody StreamingConvertRequest request){
        return Response.success(streamingService.uploadStreaming(request.sessionId()));
    }

}
