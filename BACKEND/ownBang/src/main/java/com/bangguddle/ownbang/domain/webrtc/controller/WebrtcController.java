package com.bangguddle.ownbang.domain.webrtc.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.ConnectionType;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.Recording;
import io.openvidu.java.client.RecordingProperties;
import io.openvidu.java.client.Session;

@RestController
@RequestMapping("/api/webrtcs")
public class WebrtcController {

    // OpenVidu SDK
    private final OpenVidu openVidu;

    // session name과 seesion 객체 매핑
    private final Map<String, Session> mapSessions = new ConcurrentHashMap<>();
    // session name과 tokens 매핑
    // (tokens: 사용자와 role 매핑)
    private final Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();
    // session과 recording 객체 매핑
    private final Map<String, Boolean> sessionRecordings = new ConcurrentHashMap<>();

    // Openvidu server url
    private final String OPENVIDU_URL;
    // Openvidu server password
    private final String SECRET;

    public WebrtcController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
        this.SECRET = secret;
        this.OPENVIDU_URL = openviduUrl;
        this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
    }

    /*******************/
    /*** Session API ***/
    /*******************/

    @RequestMapping(value = "/get-token", method = RequestMethod.POST)
    public ResponseEntity<Response<String>> getToken(@RequestBody Map<String, Object> sessionNameParam) {

        System.out.println("Getting sessionId and token | {sessionName}=" + sessionNameParam);

        // session 확인
        String sessionName = (String) sessionNameParam.get("sessionName");

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

                // token 반환
                return Response.success(new SuccessResponse<>(SuccessCode.GET_TOKEN_SUCCESS, token));

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

            // token 반환
            return Response.success(new SuccessResponse<>(SuccessCode.GET_TOKEN_SUCCESS, token));

        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

   

}
