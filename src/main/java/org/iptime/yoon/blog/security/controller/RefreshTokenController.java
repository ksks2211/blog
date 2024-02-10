package org.iptime.yoon.blog.security.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.user.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @Value("${auth.refresh-token.refresh-exp-hours}")
    private int REFRESH_EXP_HOURS;

    @Value("${auth.refresh-token.name}")
    private String refreshTokenName;


    @PostMapping("/refresh")
    @PreAuthorize("isAuthenticated()")
    public void publishRefreshToken(@AuthenticationPrincipal JwtUser jwtUser, HttpServletResponse response){

        String refreshToken = refreshTokenService.createToken(jwtUser.getId());
        Cookie cookie = new Cookie(refreshTokenName,refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/token/renew");
        cookie.setMaxAge(60*60*REFRESH_EXP_HOURS);
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        log.info("Refresh Token Issued : {}",refreshToken);

    }



    @DeleteMapping("/refresh")
    public void deleteRefreshToken(@AuthenticationPrincipal JwtUser jwtUser,HttpServletResponse response){
        refreshTokenService.removeTokenByUserId(jwtUser.getId());

        Cookie cookie = new Cookie(refreshTokenName,null);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/token/renew");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}
