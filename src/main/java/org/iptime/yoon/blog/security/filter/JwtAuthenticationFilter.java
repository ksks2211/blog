package org.iptime.yoon.blog.security.filter;

import com.google.common.net.HttpHeaders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.security.jwt.JwtManager;
import org.iptime.yoon.blog.security.jwt.JwtVerifyResult;
import org.iptime.yoon.blog.user.mapper.UserMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author rival
 * @since 2023-08-14
 */

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            JwtVerifyResult jwtVerifyResult = jwtManager.verifyToken(token);

            if (jwtVerifyResult.isVerified()) {

                JwtUser user = UserMapper.fromJwtResultToJwtUser(jwtVerifyResult);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
                );

                authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);

                log.info("User(={}) is authenticated",user.getUsername());
            } else if (jwtVerifyResult.isDecoded()) {
                log.info("User(={}) fail to authenticate",jwtVerifyResult.getSubject());
            } else {
                log.info("Invalid authentication attempt");
            }
        } else {
            log.info("Request without AUTHORIZATION header");
        }

        filterChain.doFilter(request, response);
    }

}
