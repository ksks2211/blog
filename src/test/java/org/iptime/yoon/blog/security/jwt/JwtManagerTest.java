package org.iptime.yoon.blog.security.jwt;

import org.iptime.yoon.blog.security.dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtManagerTest {

    @InjectMocks
    private JwtManager jwtManager;

    @Mock
    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setting mock values for JWT fields
        ReflectionTestUtils.setField(jwtManager, "JWT_AUTH_EXP_MINUTES", 120L);
        ReflectionTestUtils.setField(jwtManager, "JWT_SECRET_KEY", "your-secret-key");
        ReflectionTestUtils.setField(jwtManager, "JWT_ISSUER", "your-issuer");

        // Initialize after setting up mock values
        jwtManager.init();
    }

    @Test
    public void testCreateToken() {
        when(mockUser.getUsername()).thenReturn("testuser");
        when(mockUser.getAuthorities()).thenReturn(List.of(() -> "ROLE_USER"));

        String token = jwtManager.createToken(mockUser);

        assertNotNull(token, "Token should not be null");
    }

    @Test
    public void testVerifyValidToken() {
        when(mockUser.getUsername()).thenReturn("testuser");
        when(mockUser.getAuthorities()).thenReturn(List.of(() -> "ROLE_USER"));

        String token = jwtManager.createToken(mockUser);

        JwtVerifyResult result = jwtManager.verifyToken(token);

        assertTrue(result.isVerified(), "Token should be verified");
        assertTrue(result.isDecoded(), "Token should be decoded");
        assertEquals("testuser", result.getSubject());
        assertTrue(result.getAuthorities().contains("ROLE_USER"));
    }

    @Test
    public void testVerifyInvalidToken() {
        String invalidToken = "invalid.token.here";

        JwtVerifyResult result = jwtManager.verifyToken(invalidToken);

        assertFalse(result.isVerified(), "Token should not be verified");
        assertFalse(result.isDecoded(), "Token should not be decoded");
        assertNull(result.getSubject(), "Subject should be null for invalid token");
    }
}
