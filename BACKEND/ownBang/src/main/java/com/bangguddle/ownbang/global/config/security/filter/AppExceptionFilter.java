package com.bangguddle.ownbang.global.config.security.filter;

import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.enums.NoneResponse;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.bangguddle.ownbang.global.enums.ErrorCode.TOKEN_INVALID;

@Component
public class AppExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            doFilter(request, response, filterChain);
        } catch (JwtException e){
            writeHttpErrorResponse(response, TOKEN_INVALID);
        } catch (AppException e) {
            writeHttpErrorResponse(response, e.getErrorCode());
        }
    }

    public static void writeHttpErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Response<NoneResponse> errorResponse = Response.error(errorCode, NoneResponse.NONE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }
}
