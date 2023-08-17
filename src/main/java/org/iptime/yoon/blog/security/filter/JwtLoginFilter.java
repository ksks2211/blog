package org.iptime.yoon.blog.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.dto.res.ErrorResDto;
import org.iptime.yoon.blog.security.dto.req.BlogUserSignInReqDto;
import org.iptime.yoon.blog.security.dto.res.JwtLogInResDto;
import org.iptime.yoon.blog.security.jwt.JwtManager;
import org.iptime.yoon.blog.security.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author rival
 * @since 2023-08-14
 */

@Slf4j
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtManager jwtManager;

    private final RefreshTokenService refreshTokenService;
    public JwtLoginFilter(String filterProcessesUrl, AuthenticationManager authenticationManager, JwtManager jwtManager,RefreshTokenService refreshTokenService){
        super(new AntPathRequestMatcher(filterProcessesUrl,"POST"), authenticationManager);
        this.jwtManager = jwtManager;
        this.refreshTokenService=refreshTokenService;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try(InputStream inputStream = request.getInputStream()){
            BlogUserSignInReqDto credential = objectMapper.readValue(inputStream, BlogUserSignInReqDto.class);
            String username = credential.getUsername();
            String password = credential.getPassword();
            if(!StringUtils.hasText(username) || !StringUtils.hasText(password)){
                throw new UsernameNotFoundException(username);
            }

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);



            return getAuthenticationManager().authenticate(authRequest);
        }
    }




    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        User user= (User)authResult.getPrincipal();

        // JWT
        String token = jwtManager.createToken(user);
        JwtLogInResDto body = JwtLogInResDto.builder()
            .message("Successfully logged in.")
            .token(token)
            .statusCode(HttpStatus.OK.value())
            .build();

        log.info("JWT Issued : {}",token);
        // Refresh Token
        String refreshToken = refreshTokenService.createToken(user.getUsername());

        log.info("Refresh Token Issued : {}",refreshToken);

        Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true);
        cookie.setPath("/refresh");

        response.addCookie(cookie);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);

        response.getOutputStream().write(objectMapper.writeValueAsBytes(body));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        ErrorResDto body = ErrorResDto.builder()
            .message("Check your username and password.")
            .error("Authentication failed.")
            .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        response.getOutputStream().write(objectMapper.writeValueAsBytes(body));
    }
}
