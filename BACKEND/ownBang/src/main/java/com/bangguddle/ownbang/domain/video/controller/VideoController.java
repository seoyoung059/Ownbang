package com.bangguddle.ownbang.domain.video.controller;

import com.bangguddle.ownbang.domain.video.dto.VideoSearchResponse;
import com.bangguddle.ownbang.domain.video.service.VideoService;
import com.bangguddle.ownbang.global.response.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping("/{videoId}")
    public ResponseEntity<Response<VideoSearchResponse>> getVideo(@AuthenticationPrincipal Long userId,
                                                                  @PathVariable(name = "videoId") @Positive @Valid Long videoId){
        return Response.success(videoService.getVideo(userId, videoId));
    }
}
