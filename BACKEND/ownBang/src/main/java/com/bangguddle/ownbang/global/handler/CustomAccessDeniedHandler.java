package com.bangguddle.ownbang.global.handler;

import com.bangguddle.ownbang.global.utils.JsonResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static com.bangguddle.ownbang.global.enums.ErrorCode.ACCESS_DENIED;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        JsonResponseUtils.writeHttpErrorResponse(response, ACCESS_DENIED);
    }
}
