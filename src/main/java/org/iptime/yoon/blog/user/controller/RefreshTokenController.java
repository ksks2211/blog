package org.iptime.yoon.blog.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.user.service.RefreshTokenService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rival
 * @since 2024-01-15
 */

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;



    @PostMapping("/refresh")
    @PreAuthorize("isAuthenticated()")
    public void publishRefreshToken(@AuthenticationPrincipal JwtUser jwtUser, HttpServletResponse response){

        String refreshToken = refreshTokenService.createToken(jwtUser.getId());
        Cookie cookie = new Cookie("refresh-token",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/token/renew");
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        log.info("Refresh Token Issued : {}",refreshToken);

    }

}
