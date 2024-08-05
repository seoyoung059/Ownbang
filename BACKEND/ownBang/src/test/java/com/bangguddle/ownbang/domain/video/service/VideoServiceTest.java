package com.bangguddle.ownbang.domain.video.service;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.video.dto.VideoRecordRequest;
import com.bangguddle.ownbang.domain.video.dto.VideoSearchResponse;
import com.bangguddle.ownbang.domain.video.dto.VideoUpdateRequest;
import com.bangguddle.ownbang.domain.video.entity.Video;
import com.bangguddle.ownbang.domain.video.entity.VideoStatus;
import com.bangguddle.ownbang.domain.video.repository.VideoRepository;
import com.bangguddle.ownbang.domain.video.service.impl.VideoServiceImpl;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private Reservation reservation;
    @Mock
    private Video video;

    @InjectMocks
    private VideoServiceImpl videoService;

    private VideoRecordRequest makeRecordRequest(Long reservationId, String videoUrl, VideoStatus videoStatus){
        return VideoRecordRequest.builder()
                .reservationId(reservationId)
                .videoUrl(videoUrl)
                .videoStatus(videoStatus)
                .build();
    }

    private VideoSearchResponse makeSearchResponse(Long videoId, Long reservationId, String videoUrl, VideoStatus videoStatus){
        return VideoSearchResponse.builder()
                .videoId(videoId)
                .reservationId(reservationId)
                .videoUrl(videoUrl)
                .videoStatus(videoStatus)
                .build();
    }

    private VideoUpdateRequest makeUpdateRequest(String videoUrl, VideoStatus videoStatus){
        return VideoUpdateRequest.builder()
                .videoUrl(videoUrl)
                .videoStatus(videoStatus)
                .build();
    }

    @Test
    @DisplayName("영상 저장 성공")
    void 영상_저장_성공() throws Exception {
        // given
        Long reservationId = 10L;
        String videoUrl = "VIDEO_TEST_URL";
        VideoRecordRequest request = makeRecordRequest(reservationId, videoUrl, VideoStatus.RECORDING);
        SuccessResponse success = new SuccessResponse<>(VIDEO_RECORD_SUCCESS,NoneResponse.NONE);

        // when
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        when(videoRepository.findByReservationId(reservationId)).thenReturn(Optional.empty());

        // then
        SuccessResponse response = videoService.registerVideo(request);

        assertThat(response)
                .isNotInstanceOf(AppException.class)
                .isInstanceOf(SuccessResponse.class)
                .isEqualTo(success);

        // verify
        verify(reservationRepository, times(1)).findById(any());
        verify(videoRepository, times(1)).findByReservationId(any());
        verify(videoRepository, times(1)).save(any());
    }
    
    @Test
    @DisplayName("영상 저장 실패 - 없는 예약 번호")
    void 영상_저장_실패__없는_예약_번호() throws Exception {
        // given
        Long invalidReservationId = 10L;
        String videoUrl = "VIDEO_TEST_URL";
        VideoRecordRequest eRequest = makeRecordRequest(invalidReservationId, videoUrl, VideoStatus.RECORDING);

        // when
        when(reservationRepository.findById(invalidReservationId)).thenReturn(Optional.empty());

        // then
        Throwable thrown = catchThrowable(() -> videoService.registerVideo(eRequest));

        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_NOT_FOUND);

        // verify
        verify(reservationRepository, times(1)).findById(any());
        verify(videoRepository, never()).findByReservationId(any());
        verify(videoRepository, never()).save(any());
    }

    @Test
    @DisplayName("영상 저장 실패 - 확정된 예약이 아닌 경우")
    void 영상_저장_실패__확정된_예약이_아닌_경우() throws Exception {
        // given
        Long invalidReservationId = 10L;
        String videoUrl = "VIDEO_TEST_URL";
        VideoRecordRequest eRequest = makeRecordRequest(invalidReservationId, videoUrl, VideoStatus.RECORDING);

        // when
        when(reservationRepository.findById(invalidReservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getStatus()).thenReturn(ReservationStatus.APPLYED);

        // then
        Throwable thrown = catchThrowable(() -> videoService.registerVideo(eRequest));

        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_STATUS_NOT_CONFIRMED);

        // verify
        verify(reservationRepository, times(1)).findById(any());
        verify(videoRepository, never()).findByReservationId(any());
        verify(videoRepository, never()).save(any());
    }

    @Test
    @DisplayName("영상 저장 실패 - 취소된 예약인 경우")
    void 영상_저장_실패__취소된_예약인_경우() throws Exception {
        // given
        Long invalidReservationId = 10L;
        String videoUrl = "VIDEO_TEST_URL";
        VideoRecordRequest eRequest = makeRecordRequest(invalidReservationId, videoUrl, VideoStatus.RECORDING);

        // when
        when(reservationRepository.findById(invalidReservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getStatus()).thenReturn(ReservationStatus.CANCELED);

        // then
        Throwable thrown = catchThrowable(() -> videoService.registerVideo(eRequest));

        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", RESERVATION_STATUS_NOT_CONFIRMED);

        // verify
        verify(reservationRepository, times(1)).findById(any());
        verify(videoRepository, never()).findByReservationId(any());
        verify(videoRepository, never()).save(any());
    }

    @Test
    @DisplayName("영상 저장 실패 - 이미 저장된 영상")
    void 영상_저장_실패__이미_저장된_영상() throws Exception {
        // given
        Long reservationId = 10L;
        String videoUrl = "VIDEO_TEST_URL";
        VideoRecordRequest eRequest = makeRecordRequest(reservationId, videoUrl, VideoStatus.RECORDING);

        // when
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        when(videoRepository.findByReservationId(reservationId)).thenReturn(Optional.of(mock(Video.class)));

        // then
        Throwable thrown = catchThrowable(() -> videoService.registerVideo(eRequest));

        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", VIDEO_DUPLICATE);

        // verify
        verify(reservationRepository, times(1)).findById(any());
        verify(videoRepository, times(1)).findByReservationId(any());
        verify(videoRepository, never()).save(any());
    }

    @Test
    @DisplayName("영상 단건 조회 성공")
    void 영상_단건_조회_성공() throws Exception {
        // given
        Long userId = 123L;
        Long videoId = 1L;
        Long reservationId = 10L;
        String videoUrl = "VIDEO_TEST_URL";

        VideoSearchResponse result = makeSearchResponse(videoId, reservationId, videoUrl, VideoStatus.RECORDED);
        SuccessResponse success = new SuccessResponse<>(VIDEO_FIND_SUCCESS, result);

        // when
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        when(video.getVideoStatus()).thenReturn(VideoStatus.RECORDED);
        when(video.getId()).thenReturn(videoId);
        when(video.getReservation()).thenReturn(reservation);
        when(video.getVideoUrl()).thenReturn(videoUrl);
        when(reservation.getId()).thenReturn(reservationId);
        when(reservation.getUserId()).thenReturn(userId);

        // then
        SuccessResponse response = videoService.getVideo(userId, videoId);

        assertThat(response)
                .isNotInstanceOf(AppException.class)
                .isInstanceOf(SuccessResponse.class)
                .isEqualTo(success);

        // verify
        verify(videoRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("영상 단건 조회 실패 - 유효하지 않은 ID")
    void 영상_단건_조회_실패__유효하지_않은_ID() throws Exception {
        // given
        Long userId = 123L;
        Long invalidVideoId = 1L;

        // when
        when(videoRepository.findById(invalidVideoId)).thenReturn(Optional.empty());

        // then
        Throwable thrown = catchThrowable(() -> videoService.getVideo(userId, invalidVideoId));

        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);
    }

    @Test
    @DisplayName("영상 단건 조회 실패 - 녹화 중인 영상")
    void 영상_단건_조회_실패__녹화_중인_영상() throws Exception {
        // given
        Long userId = 123L;
        Long invalidVideoId = 1L;

        // when
        when(videoRepository.findById(invalidVideoId)).thenReturn(Optional.of(video));
        when(video.getVideoStatus()).thenReturn(VideoStatus.RECORDING);

        // then
        Throwable thrown = catchThrowable(() -> videoService.getVideo(userId, invalidVideoId));

        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", VIDEO_IS_BEING_RECORDED);
    }

    @Test
    @DisplayName("영상 단건 조회 실패 - 권한이 없는 영상")
    void 영상_단건_조회_실패__권한이_없는_영상() throws Exception {
        // given
        Long invalidUserId = 123L;
        Long videoId = 1L;
        Long reservationId = 10L;
        String videoUrl = "VIDEO_TEST_URL";

        // when
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        when(video.getVideoStatus()).thenReturn(VideoStatus.RECORDED);
        when(video.getReservation()).thenReturn(reservation);
        when(reservation.getUserId()).thenReturn(1L);

        // then
        Throwable thrown = catchThrowable(() -> videoService.getVideo(invalidUserId, videoId));

        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ACCESS_DENIED);
    }

    @Test
    @DisplayName("영상 수정 성공")
    void 영상_수정_성공() throws Exception {
        // given
        Long videoId = 1L;
        String newVideoUrl = "VIDEO_TEST_URL";
        VideoStatus newVideoStatus = VideoStatus.RECORDED;

        VideoUpdateRequest request = makeUpdateRequest(newVideoUrl, newVideoStatus);
        SuccessResponse success = new SuccessResponse<>(VIDEO_UPDATE_SUCCESS,NoneResponse.NONE);

        // when
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        when(video.getVideoStatus()).thenReturn(VideoStatus.RECORDING);

        // then
        SuccessResponse response = videoService.modifyVideo(request, videoId);

        assertThat(response)
                .isNotNull()
                .isNotInstanceOf(AppException.class)
                .isInstanceOf(SuccessResponse.class)
                .isEqualTo(success);

        // verify
        verify(videoRepository, times(1)).findById(any());
        verify(videoRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("영상 수정 실패 - 유효하지 않은 ID")
    void 영상_수정_실패__유효하지_않은_ID() throws Exception {
        // given
        Long invalidVideoId = 1L;
        String newVideoUrl = "VIDEO_TEST_URL";
        VideoStatus newVideoStatus = VideoStatus.RECORDED;

        VideoUpdateRequest eRequest = makeUpdateRequest(newVideoUrl, newVideoStatus);

        // when
        when(videoRepository.findById(invalidVideoId)).thenReturn(Optional.empty());

        // then
        Throwable thrown = catchThrowable(() -> videoService.modifyVideo(eRequest, invalidVideoId));

        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(videoRepository, times(1)).findById(any());
        verify(videoRepository, never()).save(any());
    }

    @Test
    @DisplayName("영상 수정 실패 - 이미 저장된 영상")
    void 영상_수정_실패__이미_저장된_영상() throws Exception {
        // given
        Long invalidVideoId = 1L;
        String newVideoUrl = "VIDEO_TEST_URL";
        VideoStatus newVideoStatus = VideoStatus.RECORDED;

        VideoUpdateRequest eRequest = makeUpdateRequest(newVideoUrl, newVideoStatus);

        // when
        when(videoRepository.findById(invalidVideoId)).thenReturn(Optional.of(video));
        when(video.getVideoStatus()).thenReturn(VideoStatus.RECORDED);

        // then
        Throwable thrown = catchThrowable(() -> videoService.modifyVideo(eRequest, invalidVideoId));

        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", VIDEO_DUPLICATE);

        // verify
        verify(videoRepository, times(1)).findById(any());
        verify(videoRepository, never()).save(any());
    }

    @Test
    @DisplayName("영상 수정 실패 - 유효하지 않은 영상 상태")
    void 영상_수정_실패__유효하지_않은_영상_상태() throws Exception {
        // given
        Long videoId = 1L;
        String newVideoUrl = "VIDEO_TEST_URL";
        VideoStatus newVideoStatus = VideoStatus.RECORDING;

        VideoUpdateRequest eRequest = makeUpdateRequest(newVideoUrl, newVideoStatus);

        // when
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        when(video.getVideoStatus()).thenReturn(VideoStatus.RECORDING);

        // then
        Throwable thrown = catchThrowable(() -> videoService.modifyVideo(eRequest, videoId));

        assertThat(thrown)
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", BAD_REQUEST);

        // verify
        verify(videoRepository, times(1)).findById(any());
        verify(videoRepository, never()).save(any());
    }
}
