package com.bangguddle.ownbang.domain.webrtc.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bangguddle.ownbang.domain.webrtc.service.WebrtcService;
import com.bangguddle.ownbang.domain.webrtc.service.impl.WebrtcServiceImpl;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.SuccessCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/get-token", method = RequestMethod.POST)
    public ResponseEntity<Response<String>> getToken(@RequestBody Map<String, Object> sessionNameParam) {
        System.out.println("Getting sessionId and token | {sessionName}=" + sessionNameParam);

        // session 확인
        String sessionName = (String) sessionNameParam.get("sessionName");

        SuccessResponse<String> successResponse = webrtcService.getToken(sessionName);
        return Response.success(successResponse);
    }


}
