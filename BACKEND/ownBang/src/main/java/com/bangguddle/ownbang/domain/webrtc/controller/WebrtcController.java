package com.bangguddle.ownbang.domain.webrtc.controller;

import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcCreateTokenRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcRemoveTokenRequest;
import com.bangguddle.ownbang.domain.webrtc.dto.WebrtcTokenResponse;
import com.bangguddle.ownbang.domain.webrtc.service.impl.WebrtcAgentService;
import com.bangguddle.ownbang.domain.webrtc.service.impl.WebrtcUserService;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.response.Response;
import com.bangguddle.ownbang.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/webrtcs")
@RequiredArgsConstructor
public class WebrtcController {

    private final WebrtcUserService webrtcUserService;
    private final WebrtcAgentService webrtcAgentService;

    /**
     * reservationId를 통해 session과 connection을 생성하고<br/>
     * 이를 연결할 수 있는 token을 반환합니다.<br/><br/>
     *
     * agent 권한을 갖는 유저만 session을 생성할 수 있습니다.
     * @param request
     * @return Response WebrtcTokenResponse
     */
    @PostMapping(value = "/get-token")
    public ResponseEntity<Response<WebrtcTokenResponse>> getToken(@RequestBody @Valid WebrtcCreateTokenRequest request,
                                                                  @AuthenticationPrincipal Long userId,
                                                                  Authentication authentication
    ) {
        Boolean isAgent = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AGENT"));

        SuccessResponse<WebrtcTokenResponse> response = isAgent
                ? webrtcAgentService.getToken(request, userId)
                : webrtcUserService.getToken(request, userId);

        return Response.success(response);
    }


    /**
     * session과 연결된 connection을 제거합니다.<br/>
     * agent 권한을 갖는 유저가 connection을 제거한 경우, 해당 session은 종료됩니다.
     * @param request
     * @param userId
     * @param authentication
     * @return Response NoneResponse
     */
    @PostMapping(value = "/remove-token")
    public ResponseEntity<Response<NoneResponse>> removeToken(@RequestBody @Valid WebrtcRemoveTokenRequest request,
                                                              @AuthenticationPrincipal Long userId,
                                                              Authentication authentication
    ){
        Boolean isAgent = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AGENT"));

        SuccessResponse<NoneResponse> response = isAgent
                ? webrtcAgentService.removeToken(request, userId)
                : webrtcUserService.removeToken(request, userId);

        return Response.success(response);
    }
}
