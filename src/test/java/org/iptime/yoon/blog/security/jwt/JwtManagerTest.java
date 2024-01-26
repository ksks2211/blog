package org.iptime.yoon.blog.security.jwt;

import org.apache.commons.lang3.StringUtils;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@TestPropertySource(
    properties = {
        "auth.jwt.auth-exp-minutes=120",
        "auth.jwt.secret-key=JwtSecretKey",
        "auth.jwt.issuer=JwtIssuer"
    }
)
public class JwtManagerTest {

    @Autowired
    private JwtManager jwtManager;

    @TestConfiguration
    static class TestConfig{
        @Bean
        public JwtManager jwtManager(){
            return new JwtManager();
        }
    }



    public static JwtUser createJwtUser(String username, Long id){
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return JwtUser.builder()
            .id(id)
            .username(username)
            .displayName(username)
            .authorities(authorities)
            .build();
    }

    @Test
    public void createToken(){

        String username = "example";
        Long id = 1L;
        JwtUser user = createJwtUser(username, id);
        String token = jwtManager.createToken(user);

        assertNotNull(token);
        assertTrue(StringUtils.isNotBlank(token));
    }


    @Test
    public void verifyToken(){
        // Given
        String username = "example";
        Long id = 1L;
        JwtUser user = createJwtUser(username, id);
        String token = jwtManager.createToken(user);

        // when
        JwtVerifyResult jwtVerifyResult = jwtManager.verifyToken(token);

        assertTrue(jwtVerifyResult.isDecoded());
        assertTrue(jwtVerifyResult.isVerified());
        assertEquals(id, jwtVerifyResult.getId());
        assertEquals(username, jwtVerifyResult.getSubject());
    }


    @Test
    public void verifyExpiredToken(){
        // Given
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBRE1JTiIsImV4cCI6MTcwNTQ2OTg2OSwiaXNzIjoiSnd0SXNzdWVyIiwiaWF0IjoxNzA1NDYyNjY5LCJhdXRocyI6WyJST0xFX1VTRVIiXSwiaWQiOjEsImRpc3BsYXlOYW1lIjoiQURNSU4ifQ.BO1MKFBgZPYMCrUKeL4icr-PXA-LqnDc8I5OKJ5uYmk";

        // when
        JwtVerifyResult jwtVerifyResult = jwtManager.verifyToken(token);

        assertTrue(jwtVerifyResult.isDecoded());
        assertFalse(jwtVerifyResult.isVerified());
    }

    @Test
    public void verifyNotDecodedToken(){
        // Given
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBRE1JTiIsImV4cCI6MTcwNTQ2OTg2OSwiaXNzIjoiSnd0SXNzdWVyIiwiaWF0IjoxNzA1NDYyNjY5LCJhdXRocyI6WyJST0xFX1VTRVIiXSwiaWQiOjEsImRpc3BsYXlOYdwdqwdqwdwqdw4ifQ.BO1MKFBgZPYMCrUKeL4icr-PXA-LqnDc8I5OKJ5uYmk";
        JwtVerifyResult jwtVerifyResult = jwtManager.verifyToken(token);
        assertFalse(jwtVerifyResult.isDecoded());
    }

}
