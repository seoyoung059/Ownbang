package com.bangguddle.ownbang.domain.webrtc.service.impl;

import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcCreateRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcRemoveUserRequest;
import com.bangguddle.ownbang.domain.webrtc.service.WebrtcService;
import com.bangguddle.ownbang.domain.webrtc.service.WebrtcSessionService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.springframework.stereotype.Service;

import static com.bangguddle.ownbang.global.enums.ErrorCode.RESERVATION_NOT_FOUND;

@Service
public class WebrtcAgentService implements WebrtcService {

    private final WebrtcSessionService webrtcSessionService;
    private final ReservationRepository reservationRepository;

    WebrtcAgentService(final WebrtcSessionService webrtcSessionService,
                       final ReservationRepository reservationRepository){
        this.webrtcSessionService = webrtcSessionService;
        this.reservationRepository = reservationRepository;

    }

    @Override
    public SuccessResponse getToken(WebrtcCreateRequest request) {
        return null;
    }

    @Override
    public SuccessResponse<NoneResponse> removeToken(WebrtcRemoveUserRequest request) {
        return null;
    }

    private void validateReservation(Long reservationId){
        // 예약 repo로 접근해 Reservation 을 얻어와 확인
        reservationRepository.findById(reservationId).orElseThrow( () -> new AppException(RESERVATION_NOT_FOUND));
    }
}
