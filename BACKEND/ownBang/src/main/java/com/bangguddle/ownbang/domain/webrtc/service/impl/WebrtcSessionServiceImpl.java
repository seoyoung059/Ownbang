package com.bangguddle.ownbang.domain.webrtc.service.impl;

import com.bangguddle.ownbang.domain.webrtc.enums.UserType;
import com.bangguddle.ownbang.domain.webrtc.service.WebrtcSessionService;
import com.bangguddle.ownbang.global.handler.AppException;
import io.openvidu.java.client.*;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.bangguddle.ownbang.global.enums.ErrorCode.BAD_REQUEST;
import static com.bangguddle.ownbang.global.enums.ErrorCode.INTERNAL_SERVER_ERROR;

@Service
public class WebrtcSessionServiceImpl implements WebrtcSessionService {

    private static final OpenViduRole DEFAULT_ROLE = OpenViduRole.PUBLISHER;

    private final OpenVidu openVidu;
    private final Map<Long, Session> mapSessions;
    private final Map<Long, Map<String, UserType>> mapSessionNamesTokens;
    private final Map<Long, Boolean> sessionRecordings;

    WebrtcSessionServiceImpl(final OpenVidu openVidu){
        this.openVidu = openVidu;
        this.mapSessions = new ConcurrentHashMap<>();
        this.mapSessionNamesTokens = new ConcurrentHashMap<>();
        this.sessionRecordings = new ConcurrentHashMap<>();
    }


    @Override
    public Optional<Session> getSession(final Long reservationId) {
        return Optional.ofNullable(mapSessions.get(reservationId));
    }

    @Override
    public Optional<Session> createSession(final Long reservationId){
        try{
            // 새로운 session
            Session session = this.openVidu.createSession();
            this.mapSessions.put(reservationId, session);
            this.mapSessionNamesTokens.put(reservationId, new ConcurrentHashMap<>());

            return Optional.ofNullable(session);

        } catch (Exception e){
            throw new AppException(BAD_REQUEST);
        }
    }

    @Override
    public Optional<Session> removeSession(final Long reservationId) {
        // session 유효 확인
        if (this.mapSessions.get(reservationId) != null && this.mapSessionNamesTokens.get(reservationId) != null) {
            Session session = this.mapSessions.get(reservationId);

            try {
                session.close();
            }catch (OpenViduJavaClientException | OpenViduHttpException e){
                throw new AppException(INTERNAL_SERVER_ERROR);
            }

            this.mapSessions.remove(reservationId);
            this.mapSessionNamesTokens.remove(reservationId);
            this.sessionRecordings.remove(session.getSessionId());

            return Optional.of(session);
        } else {
            // session 없는 경우
            throw new AppException(BAD_REQUEST);
        }
    }


    @Override
    public Optional<String> getToken(final Long reservationId, final UserType userType) {
        // session 유효 확인
        if (this.mapSessions.containsKey(reservationId) && this.mapSessionNamesTokens.containsKey(reservationId)) {
            // userType과 role이 같은  token 반환
            return this.mapSessionNamesTokens.get(reservationId)
                    .entrySet().stream()
                    .filter(entry -> entry.getValue().equals(userType))
                    .map(Map.Entry::getKey)
                    .findFirst();
        }
        return Optional.empty();
    }


    @Override
    public Optional<String> createToken(final Long reservationId, final UserType userType){

        // connection properties 설정
        ConnectionProperties connectionProperties = new ConnectionProperties.Builder()
                .type(ConnectionType.WEBRTC)
                .role(DEFAULT_ROLE)
                .data(userType.name())
                .build();

        try {
            String token = this.mapSessions.get(reservationId).createConnection(connectionProperties).getToken();
            this.mapSessionNamesTokens.get(reservationId).put(token, userType);

            return Optional.of(token);

        }catch (OpenViduJavaClientException e1) {
            throw new AppException(INTERNAL_SERVER_ERROR);

        } catch (OpenViduHttpException e2) {
            if (404 == e2.getStatus()) {
                // 더이상 유효하지 않은 session
                // 유저가 남아있을 수 있음 -> 삭제
                this.mapSessions.remove(reservationId);
                this.mapSessionNamesTokens.remove(reservationId);
            }
            throw new AppException(INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public Optional<String> removeToken(final Long reservationId, final String token){

        if (this.mapSessions.get(reservationId) != null && this.mapSessionNamesTokens.get(reservationId) != null) {
            if (this.mapSessionNamesTokens.get(reservationId).remove(token) != null) {
                return Optional.of(token);
            }else {
                throw new AppException(INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new AppException(BAD_REQUEST);
        }
    }
}
