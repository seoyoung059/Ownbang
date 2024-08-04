package com.bangguddle.ownbang.domain.video.service.impl;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.video.dto.VideoRecordRequest;
import com.bangguddle.ownbang.domain.video.dto.VideoSearchResponse;
import com.bangguddle.ownbang.domain.video.dto.VideoUpdateRequest;
import com.bangguddle.ownbang.domain.video.entity.Video;
import com.bangguddle.ownbang.domain.video.entity.VideoStatus;
import com.bangguddle.ownbang.domain.video.repository.VideoRepository;
import com.bangguddle.ownbang.domain.video.service.VideoService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private ReservationRepository reservationRepository;
    private VideoRepository videoRepository;

    /**
     * 녹화된 영상을 조회하는 메소드.<br/>
     * 영상 상태가 RECORDED 인 경우만 정상 반환
     * @param videoId
     * @return VideoSearchResponse
     */
    @Override
    public SuccessResponse<VideoSearchResponse> getVideo(final Long videoId) {
        // 영상 유효성 검사
        Video video = validateVideo(videoId);
        if(video.getVideoStatus() == VideoStatus.RECORDING){
            throw new AppException(VIDEO_IS_BEING_RECORDED);
        }

        return new SuccessResponse<>(VIDEO_FIND_SUCCESS, VideoSearchResponse.from(video));
    }

    /**
     * 녹화 영상을 등록하는 메소드.<br/>
     * WebRTC Service에서 호출됨.
     * @param request
     * @return NoneResponse
     */
    @Override
    public SuccessResponse<NoneResponse> registerVideo(final VideoRecordRequest request) {
        // 예약 유효성 검사
        Long reservationId = request.reservationId();
        Reservation reservation = validateReservation(reservationId);

        // 기존 녹화 유효성 검사
        validateVideoByReservation(reservationId);
        
        // request 유효성 검사
        if(request.videoUrl() == null
                || request.videoUrl().equals("")
                || request.videoStatus() != VideoStatus.RECORDING
        ){
            throw new AppException(BAD_REQUEST);
        }

        // 녹화 상태 저장
        Video video = request.toEntity(reservation);
        videoRepository.save(video);
        
        // 반환
        return new SuccessResponse<>(VIDEO_RECORD_SUCCESS,NoneResponse.NONE);
    }

    /**
     * 녹화 영상의 URL과 상태를 수정하는 메소드 <br/>
     * 기존 상태가 RECORDING 인 경우만 수정 가능
     * @param request
     * @param videoId
     * @return NoneResponse
     */
    @Override
    public SuccessResponse<NoneResponse> modifyVideo(final VideoUpdateRequest request, final Long videoId) {
        // 영상 유효성 검사
        Video video = validateVideo(videoId);
        if(video.getVideoStatus() != VideoStatus.RECORDING){
            throw new AppException(VIDEO_DUPLICATE);
        }

        // request 유효성 검사
        if(request.videoUrl() == null
                || request.videoUrl().equals("")
                || request.videoStatus() == VideoStatus.RECORDING
        ){
            throw new AppException(BAD_REQUEST);
        }

        // 업데이트
        video.update(request.videoUrl(), request.videoStatus());
        videoRepository.save(video);

        return new SuccessResponse<>(VIDEO_UPDATE_SUCCESS,NoneResponse.NONE);
    }

    // 해당 예약이 유효한지 확인
    private Reservation validateReservation(final Long reservationId){
        // 예약 repo로 접근해 Reservation 을 얻어와 확인
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new AppException(RESERVATION_NOT_FOUND)
        );

        if(reservation.getStatus() != ReservationStatus.CONFIRMED){
            throw new AppException(RESERVATION_STATUS_NOT_CONFIRMED);
        }

        return reservation;
    }

    // 해당 예약과 연결된 녹화 영상 유무 확인
    private void validateVideoByReservation(final Long reservationId){
        videoRepository.findByReservationId(reservationId)
                .ifPresent(i -> {throw new AppException(VIDEO_DUPLICATE);});
    }

    // 녹화 영상 유무 확인
    private Video validateVideo(final Long videoId){
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new AppException(BAD_REQUEST));
    }
}
