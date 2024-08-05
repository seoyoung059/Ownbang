package com.bangguddle.ownbang.global.config.security.filter;


import com.bangguddle.ownbang.domain.user.entity.User;
import com.bangguddle.ownbang.domain.user.repository.UserRepository;
import com.bangguddle.ownbang.global.config.security.JwtProvider;
import com.bangguddle.ownbang.global.dto.Tokens;
import com.bangguddle.ownbang.global.enums.ErrorCode;
import com.bangguddle.ownbang.global.handler.AppException;
import com.bangguddle.ownbang.global.repository.RedisRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final String[] REQUIRE_USER_ARRAY = {
            /* 임차인 권한 필요 URL */
            "/bookmarks",
            "/agents/auths",
            "/get-token", "/remove-token",
            "/auths/password-check",
            "/videos",
            "/checklists"
    };
    private static final String[] REQUIRE_AGENT_ARRAY = {
            /* 중개인 권한 필요 URL */
            "/rooms/agents",
    };
    private static final String HEADER_PREFIX = "Bearer ";
    private static final int TOKEN_SPLIT_INDEX = 7;
    private final RedisRepository redisRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (isNonAuthenticatedUri(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        if(header == null || !header.startsWith(HEADER_PREFIX))
            throw new AppException(ErrorCode.TOKEN_INVALID);

        final String token = header.substring(TOKEN_SPLIT_INDEX);
        long userId = jwtProvider.parseUserId(token);
        Tokens validTokens = redisRepository.getValidTokens(userId);
        if (!validTokens.accessToken().equals(token)) throw new AppException(ErrorCode.TOKEN_INVALID);

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        if (jwtProvider.isValid(token, userId)) authenticate(request, userId);

        chain.doFilter(request, response);
    }


    private boolean isNonAuthenticatedUri(String uri) {
        return !(Arrays.stream(REQUIRE_AGENT_ARRAY).anyMatch(uri::contains)
                || Arrays.stream(REQUIRE_USER_ARRAY).anyMatch(uri::contains));
    }

    private void authenticate(HttpServletRequest request, long userId) {
        User user = userRepository.getById(userId);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (user.isAgent()) authorities.add(new SimpleGrantedAuthority("ROLE_AGENT"));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);
    }
}
