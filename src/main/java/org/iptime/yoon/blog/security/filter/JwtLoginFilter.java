package org.iptime.yoon.blog.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.security.dto.LogInRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

    private final ObjectMapper objectMapper;

    public JwtLoginFilter(String filterProcessesUrl, AuthenticationManager authenticationManager, ObjectMapper objectMapper){
        super(new AntPathRequestMatcher(filterProcessesUrl,"POST"), authenticationManager);
        this.objectMapper = objectMapper;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try(InputStream inputStream = request.getInputStream()){
            LogInRequest credential = objectMapper.readValue(inputStream, LogInRequest.class);
            String username = credential.getUsername();
            String password = credential.getPassword();
            if(!StringUtils.hasText(username) || !StringUtils.hasText(password)){
                throw new UsernameNotFoundException(username);
            }

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            return getAuthenticationManager().authenticate(authRequest);
        }
    }

}
