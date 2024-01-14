package org.iptime.yoon.blog.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.security.dto.LogInSuccessResponse;
import org.iptime.yoon.blog.security.exception.InvalidRefreshTokenException;
import org.iptime.yoon.blog.security.jwt.JwtManager;
import org.iptime.yoon.blog.user.service.RefreshTokenService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author rival
 * @since 2023-08-17
 */
@Slf4j
public class JwtRefreshFilter extends OncePerRequestFilter {


    private final JwtManager jwtManager;
    private final String refreshURI;
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper;

    public JwtRefreshFilter(String refreshURI, RefreshTokenService refreshTokenService, JwtManager jwtManager, ObjectMapper objectMapper) {
        this.refreshURI = refreshURI;
        this.refreshTokenService = refreshTokenService;
        this.jwtManager = jwtManager;
        this.objectMapper = objectMapper;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        if (refreshURI.equals(request.getRequestURI())&&request.getMethod().equalsIgnoreCase("POST")) {
            String refreshToken = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refresh-token".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                    }
                }
            }

            if (refreshToken == null) throw new InvalidRefreshTokenException("Empty Refresh Token");

            JwtUser jwtUser = refreshTokenService.validateTokenAndGetJwtUser(refreshToken);

            String token = jwtManager.createToken(jwtUser);
            LogInSuccessResponse body = LogInSuccessResponse.builder()
                .token(token)
                .username(jwtUser.getUsername())
                .displayName(jwtUser.getDisplayName())
                .build();

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().write(objectMapper.writeValueAsBytes(body));

            log.info("Jwt refreshed {}", token);

        } else {
            filterChain.doFilter(request, response);
        }
    }
}
