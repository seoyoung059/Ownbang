package com.bangguddle.ownbang.domain.webrtc.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcCreateRequest;
import com.bangguddle.ownbang.domain.webrtc.service.WebrtcService;
import com.bangguddle.ownbang.domain.webrtc.service.impl.WebrtcServiceImpl;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequiredArgsConstructor
public class WebrtcController {

    private final WebrtcService webrtcService;

    /*******************/
    /*** Session API ***/
    /*******************/

    /**
     * SessionName을 통해 session과 connection을 생성하고<br/>
     * 이를 연결할 수 있는 token을 반환합니다.
     * @param request
     * @return ResponseEntity
     */
    @RequestMapping(value = "/get-token", method = RequestMethod.POST)
    public ResponseEntity<Response<String>> getToken(@RequestBody @Valid WebrtcCreateRequest request) {
        SuccessResponse<String> successResponse = webrtcService.getToken(request);
        return Response.success(successResponse);
    }


    @RequestMapping(value = "/remove-user", method = RequestMethod.POST)
    public ResponseEntity<Response<String>> removeUser(@RequestBody Map<String, Object> sessionNameToken){
        System.out.println("Removing user | {sessionName, token}=" + sessionNameToken);
        
        // session 및 token 확인
        String sessionName = (String) sessionNameToken.get("sessionName");
        String token = (String) sessionNameToken.get("token");

        // session 및 token 유효성 확인
        // reservationService.isValidSessionName...

//        SuccessResponse<String> successResponse = webrtcService.removeUser(sessionName, token);
        
        return null;
    }
}
