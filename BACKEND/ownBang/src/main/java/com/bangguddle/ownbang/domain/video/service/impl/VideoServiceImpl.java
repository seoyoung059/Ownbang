package com.bangguddle.ownbang.domain.video.service.impl;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.video.dto.VideoRecordRequest;
import com.bangguddle.ownbang.domain.video.dto.VideoSearchResponse;
import com.bangguddle.ownbang.domain.video.dto.VideoUpdateRequest;
import com.bangguddle.ownbang.domain.video.entity.Video;
import com.bangguddle.ownbang.domain.video.repository.VideoRepository;
import com.bangguddle.ownbang.domain.video.service.VideoService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.*;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private ReservationRepository reservationRepository;
    private VideoRepository videoRepository;

    @Override
    public SuccessResponse<VideoSearchResponse> getVideo(Long videoId) {
        return null;
    }

    @Override
    public SuccessResponse<NoneResponse> registerVideo(VideoRecordRequest request) {
        // 예약 유효성 검사
        Long reservationId = request.reservationId();
        Reservation reservation = validateReservation(reservationId);

        // 기존 녹화 상태 유효성 검사 - private 추출
        videoRepository.findByReservationId(reservationId)
                .ifPresent(i -> {throw new AppException(VIDEO_DUPLICATE);});

        // 녹화 상태 저장
        Video video = request.toEntity(reservation);
        videoRepository.save(video);
        
        // 반환
        return new SuccessResponse<>(VIDEO_RECORD_SUCCESS,NoneResponse.NONE);
    }

    @Override
    public SuccessResponse<NoneResponse> modifyVideo(VideoUpdateRequest request) {
        return null;
    }

    private Reservation validateReservation(Long reservationId){
        // 예약 repo로 접근해 Reservation 을 얻어와 확인
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new AppException(RESERVATION_NOT_FOUND)
        );

        if(reservation.getStatus() != ReservationStatus.CONFIRMED){
            throw new AppException(RESERVATION_STATUS_NOT_CONFIRMED);
        }

        return reservation;
    }
}
