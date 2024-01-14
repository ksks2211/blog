package org.iptime.yoon.blog.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.security.auth.AuthUser;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.security.dto.LogInSuccessResponse;
import org.iptime.yoon.blog.security.jwt.JwtManager;
import org.iptime.yoon.blog.user.entity.AuthProvider;
import org.iptime.yoon.blog.user.service.BlogUserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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
    private final BlogUserService blogUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        Object principal = authentication.getPrincipal();

        // Create JwtUser from AuthUser(Local login User) or OidcUser(OAuth2 login User)
        JwtUser jwtUser = (principal instanceof AuthUser authUser) ? fromAuthUserToJwtUser(authUser) : handleOidcUser((OidcUser)principal);
        String token = jwtManager.createToken(jwtUser);
        String username = jwtUser.getUsername();

        LogInSuccessResponse body = LogInSuccessResponse.builder()
            .token(token)
            .displayName(jwtUser.getDisplayName())
            .username(username).build();
        log.info("JWT Issued for {} : {}",username, token);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(body));
    }

    private JwtUser handleOidcUser(OidcUser oidcUser) {

        AuthProvider provider = AuthProvider.GOOGLE;

        // Collection<? extends GrantedAuthority> authorities = oidcUser.getAuthorities()
        String sub = oidcUser.getAttribute("sub");
        String displayName = oidcUser.getAttribute("name");
        String email = oidcUser.getAttribute("email");


        return blogUserService.createOAuth2BlogUserIfNotExists(provider, sub, displayName, email);


    }
}
