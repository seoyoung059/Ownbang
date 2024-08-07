package com.bangguddle.ownbang.domain.webrtc.service.impl;

import com.bangguddle.ownbang.domain.webrtc.enums.UserType;
import com.bangguddle.ownbang.domain.webrtc.service.WebrtcSessionService;
import com.bangguddle.ownbang.global.handler.AppException;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.bangguddle.ownbang.global.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class WebrtcSessionServiceImpl implements WebrtcSessionService {

    private static final OpenViduRole DEFAULT_ROLE = OpenViduRole.PUBLISHER;
    private static final boolean hasAudio = true;
    private static final boolean hasVideo = true;
    private static final Recording.OutputMode outputMode = Recording.OutputMode.INDIVIDUAL;

    private final OpenVidu openVidu;
    private final Map<Long, Session> mapSessions = new ConcurrentHashMap<>();
    private final Map<Long, Map<UserType, String>> mapSessionReservationsTokens = new ConcurrentHashMap<>();
    private final Map<Long, Recording> mapSessionRecordings = new ConcurrentHashMap<>();


    @Override
    public Optional<Session> getSession(final Long reservationId) {
        return Optional.ofNullable(mapSessions.get(reservationId));
    }

    @Override
    public Optional<Session> createSession(final Long reservationId){
        if(this.mapSessions.containsKey(reservationId)){
            throw new AppException(BAD_REQUEST);
        }

        try{
            // 새로운 session
            Session session = this.openVidu.createSession();
            this.mapSessions.put(reservationId, session);
            this.mapSessionReservationsTokens.put(reservationId, new ConcurrentHashMap<>());

            return Optional.ofNullable(session);

        } catch (OpenViduHttpException | OpenViduJavaClientException e){
            // 오픈 비두 장애 발생
            throw new AppException(INTERNAL_SERVER_ERROR);
        } catch (Exception e2){
            throw new AppException(BAD_REQUEST);
        }
    }

    @Override
    public Optional<Session> removeSession(final Long reservationId) {
        // session 유효 확인
        if (validateSessionAndToken(reservationId)) {
            Session session = this.mapSessions.get(reservationId);

            try {
                session.close();
            }catch (OpenViduJavaClientException | OpenViduHttpException e){
                throw new AppException(INTERNAL_SERVER_ERROR);
            }

            this.mapSessions.remove(reservationId);
            this.mapSessionReservationsTokens.remove(reservationId);

            return Optional.of(session);
        } else {
            // session 없는 경우
            throw new AppException(BAD_REQUEST);
        }
    }


    @Override
    public Optional<String> getToken(final Long reservationId, final UserType userType) {
        // session 유효 확인
        if (validateSessionAndToken(reservationId)) {
            // userType과 role이 같은  token 반환
            return Optional.ofNullable(this.mapSessionReservationsTokens.get(reservationId).get(userType));
        }
        return Optional.empty();
    }


    @Override
    public Optional<String> createToken(final Long reservationId, final UserType userType){
        if (!validateSessionAndToken(reservationId)) {
            throw new AppException(BAD_REQUEST);
        } else if(existTokenUserType(reservationId,userType)){
            throw new AppException(WEBRTC_TOKEN_DUPLICATED);
        }

        // connection properties 설정
        ConnectionProperties connectionProperties = new ConnectionProperties.Builder()
                .type(ConnectionType.WEBRTC)
                .role(DEFAULT_ROLE)
                .data(userType.name())
                .build();

        try {
            String token = this.mapSessions.get(reservationId)
                    .createConnection(connectionProperties)
                    .getToken();

            this.mapSessionReservationsTokens.get(reservationId).put(userType, token);

            return Optional.of(token);

        }catch (OpenViduJavaClientException e1) {
            throw new AppException(INTERNAL_SERVER_ERROR);

        } catch (OpenViduHttpException e2) {
            if (404 == e2.getStatus()) {
                // 더이상 유효하지 않은 session
                // 유저가 남아있을 수 있음 -> 삭제
                this.mapSessions.remove(reservationId);
                this.mapSessionReservationsTokens.remove(reservationId);
            }
            throw new AppException(INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public Optional<String> removeToken(final Long reservationId, final String token, final UserType userType){
        if (this.mapSessions.containsKey(reservationId)
                && existTokenUserType(reservationId,userType)
        ) {
            if (this.mapSessionReservationsTokens.get(reservationId).get(userType).equals(token)
                    && this.mapSessionReservationsTokens.get(reservationId).remove(userType) != null
            ) {
                return Optional.of(token);
            }else {
                throw new AppException(BAD_REQUEST);
            }
        } else {
            throw new AppException(BAD_REQUEST);
        }
    }

    @Override
    public Optional<Recording> startRecord(final Long reservationId) {
        if(!mapSessions.containsKey(reservationId)
                || mapSessionReservationsTokens.get(reservationId).size() != 2
                || mapSessionRecordings.containsKey(reservationId)
        ) {
            throw new AppException(BAD_REQUEST);
        }

        String sessionId = mapSessions.get(reservationId).getSessionId();

        RecordingProperties properties = new RecordingProperties.Builder()
                .outputMode(outputMode)
                .hasAudio(hasAudio)
                .hasVideo(hasVideo).build();

        try {
            Recording recording = this.openVidu.startRecording(sessionId, properties);
            this.mapSessionRecordings.put(reservationId, recording);
            return Optional.of(recording);

        } catch (OpenViduHttpException e) {
            throw new AppException(WEBRTC_NO_PUBLISHER);
        } catch (OpenViduJavaClientException e2){
            throw new AppException(INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public Optional<Recording> getRecord(final Long reservationId){
        // 세션에서 녹화가 진행중인 경우 - 임차인 재입장 고려
        System.out.println("Record 상태: " + mapSessionRecordings.get(reservationId));
        return Optional.ofNullable(mapSessionRecordings.get(reservationId));
    }

    @Override
    public Optional<Recording> stopRecord(final Long reservationId) {
        validateSessionAndRecord(reservationId);

        String recordingId = mapSessionRecordings.get(reservationId).getId();

        try {
            Recording recording = this.openVidu.stopRecording(recordingId);
            return Optional.of(recording);

        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            throw new AppException(INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<Recording> deleteRecord(final Long reservationId) {
        validateSessionAndRecord(reservationId);

        String recordingId = mapSessionRecordings.get(reservationId).getId();

        try {
            this.openVidu.deleteRecording(recordingId);
            Recording recording = this.mapSessionRecordings.remove(reservationId);
            return Optional.of(recording);

        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            throw new AppException(INTERNAL_SERVER_ERROR);
        }
    }

    private Boolean validateSessionAndToken(final Long reservationId){
        return this.mapSessions.containsKey(reservationId)
                && this.mapSessionReservationsTokens.containsKey(reservationId);
    }

    private Boolean existTokenUserType(Long reservationId, UserType userType){
        return this.mapSessionReservationsTokens.get(reservationId).containsKey(userType);
    }

    private void validateSessionAndRecord(Long reservationId){
        if(!mapSessions.containsKey(reservationId)
                || !mapSessionRecordings.containsKey(reservationId)
        ) {
            throw new AppException(BAD_REQUEST);
        }
    }
}
