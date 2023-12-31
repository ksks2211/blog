package org.iptime.yoon.blog.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.security.auth.AuthUser;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.security.dto.LogInSuccessResponse;
import org.iptime.yoon.blog.security.jwt.JwtManager;
import org.iptime.yoon.blog.user.service.RefreshTokenService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

import static org.iptime.yoon.blog.user.mapper.UserMapper.fromAuthUserToJwtUser;

/**
 * @author rival
 * @since 2023-12-31
 */

@RequiredArgsConstructor
@Slf4j
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtManager jwtManager;
    private final ObjectMapper objectMapper;
    private final RefreshTokenService refreshTokenService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        Object principal = authentication.getPrincipal();
        AuthUser authUser = (AuthUser) principal;
        JwtUser jwtUser = fromAuthUserToJwtUser(authUser);

        String token = jwtManager.createToken(jwtUser);
        String username = jwtUser.getUsername();
        LogInSuccessResponse body = LogInSuccessResponse.builder()
            .token(token)
            .username(username).build();

        log.info("JWT Issued for {} : {}",username, token);
        String refreshToken = refreshTokenService.createToken(jwtUser.getId());
        log.info("Refresh Token Issued : {}",refreshToken);
        Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/refresh");

        response.addCookie(cookie);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(body));
    }
}
