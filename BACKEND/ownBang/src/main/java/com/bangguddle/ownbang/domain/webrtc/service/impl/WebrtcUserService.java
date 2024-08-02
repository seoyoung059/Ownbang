package com.bangguddle.ownbang.domain.webrtc.service.impl;

import com.bangguddle.ownbang.domain.reservation.entity.Reservation;
import com.bangguddle.ownbang.domain.reservation.entity.ReservationStatus;
import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
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
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;
import static com.bangguddle.ownbang.global.enums.SuccessCode.GET_TOKEN_SUCCESS;
import static com.bangguddle.ownbang.global.enums.SuccessCode.REMOVE_TOKEN_SUCCESS;

@Service
public class WebrtcUserService implements WebrtcService {

    private final WebrtcSessionService webrtcSessionService;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public WebrtcUserService(final WebrtcSessionService webrtcSessionService,
                             final ReservationRepository reservationRepository,
                             final UserRepository userRepository
    ){
        this.webrtcSessionService = webrtcSessionService;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SuccessResponse<WebrtcTokenResponse> getToken(final WebrtcCreateTokenRequest request, final Long userId) {
        // userId 유효성 검사
        userRepository.getById(userId);

        // reservationId 유효성 검사
        Long reservationId = request.reservationId();
        validateReservation(reservationId);

        // session 유효성 검사 - private 전환
        webrtcSessionService.getSession(reservationId).orElseThrow(
                () -> new AppException(BAD_REQUEST));

        // token 생성
        String token = webrtcSessionService.createToken(reservationId, UserType.ROLE_USER)
                .orElseThrow(() -> new AppException(INTERNAL_SERVER_ERROR));

        // 영상 녹화 중지
        Optional<Recording> recording = webrtcSessionService.stopRecord(reservationId);

        // record 저장 - 추후 추가

        // response 반환
        return new SuccessResponse<>(GET_TOKEN_SUCCESS, new WebrtcTokenResponse(token));
    }

    @Override
    public SuccessResponse<NoneResponse> removeToken(WebrtcRemoveTokenRequest request, final Long userId) {
        // userId 유효성 검사
        userRepository.getById(userId);

        // reservationId 유효성 검사
        Long reservationId = request.reservationId();
        validateReservation(reservationId);

        // session 유효성 검사 - private 전환
        webrtcSessionService.getSession(reservationId).orElseThrow(
                () -> new AppException(BAD_REQUEST));

        // token 제거
        String token = request.token();
        webrtcSessionService.removeToken(reservationId, token, UserType.ROLE_USER);

        // response 반환
        return new SuccessResponse<>(REMOVE_TOKEN_SUCCESS, NoneResponse.NONE);
    }


    private void validateReservation(Long reservationId){
        // 예약 repo로 접근해 Reservation 을 얻어와 확인
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new AppException(RESERVATION_NOT_FOUND)
        );

        if(reservation.getStatus() != ReservationStatus.CONFIRMED){
            throw new AppException(RESERVATION_STATUS_NOT_CONFIRMED);
        }
    }
}
