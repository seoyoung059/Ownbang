package com.bangguddle.ownbang.domain.video.service.impl;

import com.bangguddle.ownbang.domain.video.dto.VideoRecordRequest;
import com.bangguddle.ownbang.domain.video.dto.VideoSearchResponse;
import com.bangguddle.ownbang.domain.video.dto.VideoUpdateRequest;
import com.bangguddle.ownbang.domain.video.service.VideoService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {

    @Override
    public SuccessResponse<VideoSearchResponse> getVideo(Long videoId) {
        return null;
    }

    @Override
    public SuccessResponse<NoneResponse> registVideo(VideoRecordRequest request) {
        return null;
    }

    @Override
    public SuccessResponse<NoneResponse> modifyVideo(VideoUpdateRequest request) {
        return null;
    }
}
