package com.bangguddle.ownbang.domain.webrtc.service.impl;


import com.bangguddle.ownbang.domain.reservation.repository.ReservationRepository;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcCreateRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcRemoveUserRequest;
import com.bangguddle.ownbang.domain.webrtc.service.WebrtcService;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import io.openvidu.java.client.*;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;

@Service
public class WebrtcServiceImpl implements WebrtcService {

    private static final OpenViduRole DEFAULT_ROLE = OpenViduRole.PUBLISHER;
    private static final String USER_DATA = "user_data";

    // OpenVidu SDK
    private final OpenVidu openVidu;
    // session name과 seesion 객체 매핑
    private final Map<Long, Session> mapSessions = new ConcurrentHashMap<>();
    // session name과 tokens 매핑
    private final Map<Long, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();
    // session과 recording 객체 매핑
    private final Map<Long, Boolean> sessionRecordings = new ConcurrentHashMap<>();
    // ReservationRepository
    private final ReservationRepository reservationRepository;

    public WebrtcServiceImpl(final OpenVidu openVidu, ReservationRepository reservationRepository) {
        this.openVidu = openVidu;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public SuccessResponse getToken(final WebrtcCreateRequest request) {
        // reservationId 유효성 확인
        Long reservationId = request.reservationId();
        validateReservation(reservationId);

        // 유저 role 연결
        OpenViduRole role = OpenViduRole.PUBLISHER;

        // connection properties 설정
        ConnectionProperties connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC)
                .role(role).data("user_data").build();

        // 기존 session 등록 여부 확인
        if (this.mapSessions.get(reservationId) != null) {
            // session 존재
            System.out.println("Existing session " + reservationId);
            try {
                // token 생성
                String token = this.mapSessions.get(reservationId).createConnection(connectionProperties).getToken();

                // session 과 token 연결
                this.mapSessionNamesTokens.get(reservationId).put(token, role);

                // SuccessResponse<token> 반환
                return new SuccessResponse<>(SuccessCode.GET_TOKEN_SUCCESS, token);

            } catch (OpenViduJavaClientException e1) {
                throw new AppException(ErrorCode.BAD_REQUEST);
            } catch (OpenViduHttpException e2) {
                if (404 == e2.getStatus()) {
                    // 유효하지 않은 sessionId인 경우
                    // session 을 초기화하고, 새롭게 생성
                    this.mapSessions.remove(reservationId);
                    this.mapSessionNamesTokens.remove(reservationId);
                }
            }
        }

        // 새로운 세션 생성
        System.out.println("New session " + reservationId);
        try {
            // 새로운 session 및 token 생성
            Session session = this.openVidu.createSession();
            String token = session.createConnection(connectionProperties).getToken();

            // session 과 token 저장
            this.mapSessions.put(reservationId, session);
            this.mapSessionNamesTokens.put(reservationId, new ConcurrentHashMap<>());
            this.mapSessionNamesTokens.get(reservationId).put(token, role);

            // SuccessResponse<token> 반환
            return new SuccessResponse<>(SuccessCode.GET_TOKEN_SUCCESS, token);

        } catch (Exception e) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }
    }

    @Override
    public SuccessResponse removeUser(WebrtcRemoveUserRequest request) {
        return null;
    }


    private void validateReservation(Long reservationId){
        // 예약 repo로 접근해 Reservation 을 얻어와 확인
        reservationRepository.findById(reservationId).orElseThrow( () -> new AppException(Reservation_NOT_FOUND));
    }

}
