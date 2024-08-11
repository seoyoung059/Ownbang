package com.bangguddle.ownbang.domain.webrtc.service;


import com.bangguddle.ownbang.domain.webrtc.enums.UserType;
import io.openvidu.java.client.Recording;
import io.openvidu.java.client.Session;

import java.util.Optional;

public interface WebrtcSessionService {
    Optional<Session> getSession(Long reservationId);
    Optional<Session> createSession(Long reservationId);
    Optional<Session> removeSession(Long reservationId);

    Optional<String> getToken(Long reservationId, UserType userType);
    Optional<String> createToken(Long reservationId, UserType userType);
    Optional<String> removeToken(Long reservationId, String token, UserType userType);

    Optional<Recording> startRecord(Long reservationId);
    Optional<Recording> getRecord(Long reservationId);
    Optional<Recording> stopRecord(Long reservationId);
    Optional<Recording> deleteRecord(Long reservationId);
}
