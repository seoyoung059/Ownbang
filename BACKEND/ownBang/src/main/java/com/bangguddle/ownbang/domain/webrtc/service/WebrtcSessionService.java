package com.bangguddle.ownbang.domain.webrtc.service;

import com.bangguddle.ownbang.domain.webrtc.entity.WebrtcSession;
import io.openvidu.java.client.Session;

import java.util.Optional;

public interface WebrtcSessionService {
    Optional<WebrtcSession> getSession(Long reservationId);
    Boolean removeSession(Long reservationId);
}
