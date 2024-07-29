package com.bangguddle.ownbang.domain.webrtc.service.impl;

import com.bangguddle.ownbang.domain.webrtc.entity.WebrtcSession;
import com.bangguddle.ownbang.domain.webrtc.repository.WebrtcSessionRepository;
import com.bangguddle.ownbang.domain.webrtc.service.WebrtcSessionService;
import io.openvidu.java.client.OpenVidu;

import java.util.Optional;

public class WebrtcSessionServiceImpl implements WebrtcSessionService {

    final OpenVidu openVidu;
    final WebrtcSessionRepository webrtcSessionRepository;

    WebrtcSessionServiceImpl(OpenVidu openVidu, WebrtcSessionRepository webrtcSessionRepository){
        this.openVidu = openVidu;
        this.webrtcSessionRepository = webrtcSessionRepository;
    }

    @Override
    public Optional<WebrtcSession> getSession(Long reservationId) {
        return null;
    }

    @Override
    public Boolean removeSession(Long reservationId) {
        return null;
    }
}
