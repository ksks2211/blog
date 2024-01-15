package org.iptime.yoon.blog.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.common.ErrorResponse;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.security.dto.LogInSuccessResponse;
import org.iptime.yoon.blog.security.exception.InvalidRefreshTokenException;
import org.iptime.yoon.blog.security.jwt.JwtManager;
import org.iptime.yoon.blog.user.service.RefreshTokenService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author rival
 * @since 2023-08-17
 */
@Slf4j
public class JwtRefreshFilter extends OncePerRequestFilter {

    @Value("${auth.refresh-token.name}")
    private String refreshTokenName;

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


    private String extractRefreshToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (refreshTokenName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new InvalidRefreshTokenException("Refresh Token is empty");
    }

    private boolean isRefreshRequest(HttpServletRequest request){
        return refreshURI.equals(request.getRequestURI())&&request.getMethod().equalsIgnoreCase("POST");
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        boolean isRefreshRequest = isRefreshRequest(request);
        if (isRefreshRequest) {
            try{
                String refreshToken = extractRefreshToken(request);
                validateRefreshAndIssueJwt(response, refreshToken);
            }catch(Exception e){
                log.info(e.getMessage());
                ErrorResponse body = ErrorResponse.builder().statusCode(401).message(e.getMessage()).build();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().write(objectMapper.writeValueAsBytes(body));
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void validateRefreshAndIssueJwt(@NotNull HttpServletResponse response, String refreshToken) throws IOException {
        JwtUser jwtUser = refreshTokenService.validateTokenAndGetJwtUser(refreshToken);
        String token = jwtManager.createToken(jwtUser);
        LogInSuccessResponse body = LogInSuccessResponse.builder()
            .token(token)
            .username(jwtUser.getUsername())
            .displayName(jwtUser.getDisplayName())
            .build();

        log.info("Jwt Refreshed {}", token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(body));
    }
}
