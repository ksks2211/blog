package org.iptime.yoon.blog.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.security.dto.ForbiddenAccessResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * @author rival
 * @since 2024-01-15
 */

@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ForbiddenAccessResponse body = new ForbiddenAccessResponse();
        log.debug("Access Denied Exception : {}", accessDeniedException.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(body));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
