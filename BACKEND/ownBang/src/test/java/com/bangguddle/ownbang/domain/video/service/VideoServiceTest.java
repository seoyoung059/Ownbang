package com.bangguddle.ownbang.domain.video.service;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.video.dto.VideoRecordRequest;
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

    @InjectMocks
    private VideoServiceImpl videoService;

    private VideoRecordRequest makeRecordRequest(Long reservationId, String videoUrl, VideoStatus videoStatus){
        return VideoRecordRequest.builder()
                .reservationId(reservationId)
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
}
