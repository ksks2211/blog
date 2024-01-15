package org.iptime.yoon.blog.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.security.dto.UnauthorizedAccessResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * @author rival
 * @since 2024-01-15
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        UnauthorizedAccessResponse body = new UnauthorizedAccessResponse(authException.getMessage());
        log.debug("Unauthorized Exception : {}", authException.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(body));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
