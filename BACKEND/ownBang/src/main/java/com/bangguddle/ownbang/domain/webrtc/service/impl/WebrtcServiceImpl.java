package com.bangguddle.ownbang.domain.webrtc.service.impl;


import com.bangguddle.ownbang.domain.webrtc.service.WebrtcService;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import io.openvidu.java.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebrtcServiceImpl implements WebrtcService {

    // OpenVidu SDK
    private final OpenVidu openVidu;
    // session name과 seesion 객체 매핑
    private final Map<String, Session> mapSessions = new ConcurrentHashMap<>();
    // session name과 tokens 매핑
    private final Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();
    // session과 recording 객체 매핑
    private final Map<String, Boolean> sessionRecordings = new ConcurrentHashMap<>();

    public WebrtcServiceImpl(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
        this.openVidu = new OpenVidu(openviduUrl, secret);
    }

    @Override
    public SuccessResponse getToken(String sessionName) {
        // 유저 role 연결
        OpenViduRole role = OpenViduRole.PUBLISHER;

        // connection properties 설정
        ConnectionProperties connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC)
                .role(role).data("user_data").build();

        // 기존 session 등록 여부 확인
        if (this.mapSessions.get(sessionName) != null) {
            // session 존재
            System.out.println("Existing session " + sessionName);
            try {
                // token 생성
                String token = this.mapSessions.get(sessionName).createConnection(connectionProperties).getToken();

                // session 과 token 연결
                this.mapSessionNamesTokens.get(sessionName).put(token, role);

                // SuccessResponse<token> 반환
                return new SuccessResponse<>(SuccessCode.GET_TOKEN_SUCCESS, token);

            } catch (OpenViduJavaClientException e1) {
                throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
            } catch (OpenViduHttpException e2) {
                if (404 == e2.getStatus()) {
                    // 유효하지 않은 sessionId인 경우
                    // session 을 초기화하고, 새롭게 생성
                    this.mapSessions.remove(sessionName);
                    this.mapSessionNamesTokens.remove(sessionName);
                }
            }
        }

        // New session
        System.out.println("New session " + sessionName);
        try {
            // 새로운 openvidu session 생성
            Session session = this.openVidu.createSession();
            // 새로운 token 생성
            String token = session.createConnection(connectionProperties).getToken();

            // session 과 token 저장
            this.mapSessions.put(sessionName, session);
            this.mapSessionNamesTokens.put(sessionName, new ConcurrentHashMap<>());
            this.mapSessionNamesTokens.get(sessionName).put(token, role);

            // SuccessResponse<token> 반환
            return new SuccessResponse<>(SuccessCode.GET_TOKEN_SUCCESS, token);

        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


}
