package com.bangguddle.ownbang.domain.video.controller;

import com.bangguddle.ownbang.domain.video.dto.VideoSearchResponse;
import com.bangguddle.ownbang.domain.video.entity.VideoStatus;
import com.bangguddle.ownbang.domain.video.service.impl.VideoServiceImpl;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VideoControllerTest {
    @Mock
    private VideoServiceImpl videoService;

    @InjectMocks
    private VideoController controller;

    @Test
    @DisplayName("영상 단건 조회 성공")
    void 영상_단건_조회_성공() throws Exception {
        // given
        Long userId = 1L;
        Long videoId = 2L;
        Long reservationId = 3L;
        String videoUrl = "VIDEO_TEST_URL";
        VideoStatus videoStatus = VideoStatus.RECORDED;

        VideoSearchResponse response =
                new VideoSearchResponse(videoId, reservationId, videoUrl, videoStatus);
        SuccessResponse<VideoSearchResponse> success =
                new SuccessResponse<>(VIDEO_FIND_SUCCESS, response);

        // when
        when(videoService.getVideo(userId, videoId)).thenReturn(success);

        // then
        assertThat(controller.getVideo(userId, videoId))
                .isNotInstanceOf(AppException.class)
                .isInstanceOf(ResponseEntity.class)
                .hasFieldOrPropertyWithValue("statusCode", VIDEO_FIND_SUCCESS.getHttpStatus())
                .isEqualTo(Response.success(success));
    }

    @Test
    @DisplayName("영상 단건 조회 실패 - 녹화_중인_영상")
    void 영상_단건_조회_실패__녹화_중인_영상() throws Exception {
        Long userId = 1L;
        Long invalidVideoId = 2L;

        // when
        when(videoService.getVideo(userId, invalidVideoId)).thenThrow(new AppException(VIDEO_IS_BEING_RECORDED));

        // then
        assertThatThrownBy(() -> controller.getVideo(userId, invalidVideoId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", VIDEO_IS_BEING_RECORDED);
    }

    @Test
    @DisplayName("영상 단건 조회 실패 - 권한 없는 유저")
    void 영상_단건_조회_실패__권한_없는_유저() throws Exception {
        Long userId = 1L;
        Long invalidVideoId = 2L;

        // when
        when(videoService.getVideo(userId, invalidVideoId)).thenThrow(new AppException(ACCESS_DENIED));

        // then
        assertThatThrownBy(() -> controller.getVideo(userId, invalidVideoId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ACCESS_DENIED);
    }
}
