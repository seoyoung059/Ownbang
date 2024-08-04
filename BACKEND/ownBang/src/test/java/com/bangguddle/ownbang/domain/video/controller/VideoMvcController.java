package com.bangguddle.ownbang.domain.video.controller;


import com.bangguddle.ownbang.domain.video.dto.VideoSearchResponse;
import com.bangguddle.ownbang.domain.video.entity.VideoStatus;
import com.bangguddle.ownbang.domain.video.service.impl.VideoServiceImpl;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.bangguddle.ownbang.global.enums.SuccessCode.VIDEO_FIND_SUCCESS;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VideoController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
                {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OncePerRequestFilter.class})})
public class VideoMvcController {
    @MockBean
    private VideoServiceImpl videoService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("영상 단건 조회 성공")
    void 영상_단건_조회_성공() throws Exception {
        // given
        Long videoId = 1L;
        SuccessResponse<VideoSearchResponse> success =
                new SuccessResponse<>(VIDEO_FIND_SUCCESS,
                        new VideoSearchResponse(1L, 1L, "url", VideoStatus.RECORDED));

        // when
        when(videoService.getVideo(any(), any())).thenReturn(success);

        // then
        mockMvc.perform(
                        get("/videos/{videoId}", String.valueOf(videoId))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(VIDEO_FIND_SUCCESS.name()));
    }

    @Test
    @DisplayName("영상 단건 조회 실패 - 유효하지 않은 ID")
    void 영상_단건_조회_실패__유효하지_않은_ID() throws Exception {
        // given
        Long videoId = -1L;
        SuccessResponse<VideoSearchResponse> success =
                new SuccessResponse<>(VIDEO_FIND_SUCCESS,
                        new VideoSearchResponse(1L, 1L, "url", VideoStatus.RECORDED));

        // when
        when(videoService.getVideo(any(), any())).thenReturn(success);

        // then
        mockMvc.perform(
                        get("/videos/{videoId}", String.valueOf(videoId))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isBadRequest());
    }
}
