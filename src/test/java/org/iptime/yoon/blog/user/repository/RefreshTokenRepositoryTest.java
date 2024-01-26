package org.iptime.yoon.blog.user.repository;

import org.iptime.yoon.blog.common.config.JpaConfig;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.iptime.yoon.blog.user.entity.RefreshToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.iptime.yoon.blog.user.repository.BlogUserTestHelper.createLocalUser;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rival
 * @since 2024-01-26
 */

@DataJpaTest
@Import(JpaConfig.class)
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private BlogUserRepository blogUserRepository;


    public RefreshToken createToken(BlogUser user) {
        return RefreshToken.builder()
            .user(user)
            .expiryDate(LocalDateTime.now().plusMinutes(30))
            .build();
    }

    public void createExpiredToken(int n) {

        IntStream.range(0, n).forEach(i -> {
            String username = "User" + i;
            BlogUser user = createLocalUser(blogUserRepository, username);
            RefreshToken token = RefreshToken.builder()
                .user(user)
                .expiryDate(LocalDateTime.now().minusMinutes(30))
                .build();
            refreshTokenRepository.save(token);
        });


    }

    @Test
    void createRefreshToken() {
        // Given
        String username = "example123";
        BlogUser user = createLocalUser(blogUserRepository, username);
        RefreshToken refreshToken = createToken(user);

        // When
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);


        // Then
        assertNotNull(savedToken);
        assertTrue(savedToken.getExpiryDate().isAfter(LocalDateTime.now()));
    }

    @Test
    void findByUser() {

        // Given
        String username = "example123";
        BlogUser user = createLocalUser(blogUserRepository, username);
        RefreshToken refreshToken = createToken(user);
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        String tokenId = savedToken.getId();

        // When
        Optional<RefreshToken> optional = refreshTokenRepository.findByUser(user);

        // Then
        assertTrue(optional.isPresent());
        optional.ifPresent(token -> assertEquals(tokenId, token.getId()));
    }

    @Test
    void deleteAllExpiredTokens() {
        // Given
        int numOfTokens = 3;
        createExpiredToken(numOfTokens);
        BlogUser user = createLocalUser(blogUserRepository, "NotExpiredUser");
        createToken(user);

        // When
        int deletedRows = refreshTokenRepository.deleteAllExpiredTokens();

        // Then
        assertEquals(numOfTokens, deletedRows);

    }

    @Test
    void deleteByUser() {
        // Given
        BlogUser user = createLocalUser(blogUserRepository, "NotExpiredUser");
        createToken(user);

        // When
        refreshTokenRepository.deleteByUser(user);
        Optional<RefreshToken> optional = refreshTokenRepository.findByUser(user);

        // Then
        assertFalse(optional.isPresent());

    }
}