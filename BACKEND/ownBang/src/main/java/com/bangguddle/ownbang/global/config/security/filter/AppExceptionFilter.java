package com.bangguddle.ownbang.global.config.security.filter;

import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.utils.JsonResponseUtils;
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
        } catch (JwtException e) {
            JsonResponseUtils.writeHttpErrorResponse(response, TOKEN_INVALID);
        } catch (AppException e) {
            JsonResponseUtils.writeHttpErrorResponse(response, e.getErrorCode());
        }
    }

}
