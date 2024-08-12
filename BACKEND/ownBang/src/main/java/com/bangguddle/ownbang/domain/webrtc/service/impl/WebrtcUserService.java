package com.bangguddle.ownbang.domain.webrtc.service.impl;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.domain.video.dto.VideoRecordRequest;
import com.bangguddle.ownbang.domain.video.entity.VideoStatus;
import com.bangguddle.ownbang.domain.video.service.VideoService;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcCreateTokenRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcRemoveTokenRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcTokenResponse;
import com.bangguddle.ownbang.domain.webrtc.enums.UserType;
import com.bangguddle.ownbang.domain.webrtc.service.WebrtcService;
import com.bangguddle.ownbang.domain.webrtc.service.WebrtcSessionService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import io.openvidu.java.client.Recording;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.GET_TOKEN_SUCCESS;
import static com.bangguddle.ownbang.global.enums.SuccessCode.REMOVE_TOKEN_SUCCESS;

@Service
@RequiredArgsConstructor
public class WebrtcUserService implements WebrtcService {

    private final VideoService videoService;
    private final WebrtcSessionService webrtcSessionService;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Override
    public SuccessResponse<WebrtcTokenResponse> getToken(final WebrtcCreateTokenRequest request, final Long userId) {
        // userId & reservationId 유효성 검사
        Long reservationId = request.reservationId();
        validateUserAndReservation(userId, reservationId);

        // session 유효성 검사
        validateSession(reservationId);

        // 기존 토큰 유무 확인 및 삭제
        webrtcSessionService.getToken(reservationId, UserType.ROLE_USER)
                .ifPresent(token -> {
                    System.out.println("기존 토큰 존재: " + token);
                    webrtcSessionService.removeToken(reservationId, token, UserType.ROLE_USER)
                            .orElseThrow(() -> new AppException(INTERNAL_SERVER_ERROR));
                });

        // token 생성
        String token = webrtcSessionService.createToken(reservationId, UserType.ROLE_USER)
                .orElseThrow(() -> new AppException(INTERNAL_SERVER_ERROR));

        // 기존 세션에서 녹화 진행중인 경우
        Optional<Recording> existingRecording =  webrtcSessionService.getRecord(reservationId);
        if(existingRecording.isPresent()){
            return new SuccessResponse<>(GET_TOKEN_SUCCESS,
                    new WebrtcTokenResponse(token, existingRecording.get().getCreatedAt()));
        }

        // 영상 녹화 시작
        Recording recording = webrtcSessionService.startRecord(reservationId)
                .orElseThrow(() -> new AppException(INTERNAL_SERVER_ERROR));

        // video 저장
        VideoRecordRequest videoRecordRequest = VideoRecordRequest.builder()
                .reservationId(reservationId)
                .videoUrl(recording.getSessionId()
                        )
                .videoStatus(VideoStatus.RECORDING)
                .build();
        videoService.registerVideo(videoRecordRequest);

        // response 반환
        return new SuccessResponse<>(GET_TOKEN_SUCCESS,
                new WebrtcTokenResponse(token, recording.getCreatedAt()));
    }

    @Override
    public SuccessResponse<NoneResponse> removeToken(final WebrtcRemoveTokenRequest request, final Long userId) {
        // userId & reservationId 유효성 검사
        Long reservationId = request.reservationId();
        validateUserAndReservation(userId, reservationId);

        // session 유효성 검사
        validateSession(reservationId);

        // token 제거
        String token = request.token();
        webrtcSessionService.removeToken(reservationId, token, UserType.ROLE_USER);

        // response 반환
        return new SuccessResponse<>(REMOVE_TOKEN_SUCCESS, NoneResponse.NONE);
    }


    private void validateUserAndReservation(final Long userId, final Long reservationId){
        // userId 유효성 검사
        userRepository.getById(userId);

        // 예약 repo로 접근해 Reservation 을 얻어와 확인
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new AppException(RESERVATION_NOT_FOUND)
        );

        if(reservation.getStatus() != ReservationStatus.CONFIRMED){
            throw new AppException(RESERVATION_STATUS_NOT_CONFIRMED);
        }

        if(reservation.getUser().getId() != userId){
            throw new AppException(ACCESS_DENIED);
        }
    }

    private void validateSession(final Long reservationId){
        webrtcSessionService.getSession(reservationId).orElseThrow(
                () -> new AppException(WEBRTC_SESSION_UNOPENED));
    }
}
