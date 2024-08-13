package com.bangguddle.ownbang.domain.webrtc.service.impl;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.streaming.service.StreamingService;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.domain.video.dto.VideoUpdateRequest;
import com.bangguddle.ownbang.domain.video.entity.Video;
import com.bangguddle.ownbang.domain.video.entity.VideoStatus;
import com.bangguddle.ownbang.domain.video.repository.VideoRepository;
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

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.GET_TOKEN_SUCCESS;
import static com.bangguddle.ownbang.global.enums.SuccessCode.REMOVE_TOKEN_SUCCESS;

@Service
@RequiredArgsConstructor
public class WebrtcAgentService implements WebrtcService {

    private final VideoService videoService;
    private final StreamingService streamingService;
    private final WebrtcSessionService webrtcSessionService;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    @Override
    public SuccessResponse<WebrtcTokenResponse> getToken(WebrtcCreateTokenRequest request, final Long userId) {
        // userId 유효성 검사
        userRepository.getById(userId);

        // reservationId 유효성 검사
        Long reservationId = request.reservationId();
        validateReservationAndUser(reservationId, userId);

        // session 중복 검사
        webrtcSessionService.getSession(reservationId)
                .ifPresent(i -> {throw new AppException(WEBRTC_SESSION_DUPLICATED);});

        // 이전에 종료된 세션 여부 확인
        videoRepository.findByReservationId(reservationId)
                .ifPresent(i -> {throw new AppException(WEBRTC_SESSION_CLOSED);});

        // session 생성
        webrtcSessionService.createSession(reservationId);

        // token 생성
        String token = webrtcSessionService.createToken(reservationId, UserType.ROLE_AGENT)
                .orElseThrow(() -> new AppException(INTERNAL_SERVER_ERROR));
        
        // response 반환
        return new SuccessResponse<>(GET_TOKEN_SUCCESS, new WebrtcTokenResponse(token, 0l));
    }

    @Override
    public SuccessResponse<NoneResponse> removeToken(WebrtcRemoveTokenRequest request, final Long userId) {
        // userId 유효성 검사
        userRepository.getById(userId);

        // reservationId 유효성 검사
        Long reservationId = request.reservationId();
        validateReservationAndUser(reservationId, userId);

        // session 유효성 검사
        webrtcSessionService.getSession(reservationId).orElseThrow(
                () -> new AppException(BAD_REQUEST));

        // 영상 녹화 확인
        if(webrtcSessionService.getRecord(reservationId).isEmpty()){
            // token 제거
            String token = request.token();
            webrtcSessionService.removeToken(reservationId, token, UserType.ROLE_AGENT);

            // session 제거
            webrtcSessionService.removeSession(reservationId);

            // response 반환
            return new SuccessResponse<>(REMOVE_TOKEN_SUCCESS, NoneResponse.NONE);
        }

        // 영상 녹화 중지
        Recording recording =  webrtcSessionService.stopRecord(reservationId)
                .orElseThrow(() -> new AppException(INTERNAL_SERVER_ERROR));

        // token 제거
        String token = request.token();
        webrtcSessionService.removeToken(reservationId, token, UserType.ROLE_AGENT);

        // session 제거
        webrtcSessionService.removeSession(reservationId);

        // 녹화 상태 저장: 인코딩
        Video video = videoRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new AppException(INTERNAL_SERVER_ERROR));
        VideoUpdateRequest videoUpdateRequest =
                VideoUpdateRequest.builder()
                        .videoUrl(video.getVideoUrl())
                        .videoStatus(VideoStatus.ENCODING)
                        .build();
        videoService.modifyVideo(videoUpdateRequest, video.getId());

        // record hls 변환
        streamingService.uploadStreaming(reservationId, recording.getSessionId());

        // response 반환
        return new SuccessResponse<>(REMOVE_TOKEN_SUCCESS, NoneResponse.NONE);
    }

    private void validateReservationAndUser(Long reservationId, Long userId){
        // 예약 repo로 접근해 Reservation 을 얻어와 확인
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new AppException(RESERVATION_NOT_FOUND)
        );

        if(reservation.getStatus() != ReservationStatus.CONFIRMED){
            throw new AppException(RESERVATION_STATUS_NOT_CONFIRMED);
        }

        if(userId != reservation.getRoom().getAgent().getUser().getId()){
            throw new AppException(ACCESS_DENIED);
        }
    }
}
