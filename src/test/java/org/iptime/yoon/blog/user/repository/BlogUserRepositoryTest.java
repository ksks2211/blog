package org.iptime.yoon.blog.user.repository;

import org.iptime.yoon.blog.common.config.JpaConfig;
import org.iptime.yoon.blog.user.entity.AuthProvider;
import org.iptime.yoon.blog.user.entity.BlogRole;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.iptime.yoon.blog.user.repository.BlogUserTestHelper.createLocalUser;
import static org.iptime.yoon.blog.user.repository.BlogUserTestHelper.createOAuth2User;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rival
 * @since 2024-01-26
 */

@DataJpaTest
@Import(JpaConfig.class)
class BlogUserRepositoryTest {

    @Autowired
    private BlogUserRepository blogUserRepository;





    @DisplayName("Save User")
    @Test
    public void testSaveUser(){
        // Given
        String username = "username1111";
        String email = "example123@example.com";
        BlogRole role = BlogRole.ADMIN;
        AuthProvider authProvider = AuthProvider.LOCAL;
        LocalDate dateOfBirth = LocalDate.now().minusYears(10);
        BlogUser user = BlogUser.builder()
            .username(username)
            .displayName(username)
            .dateOfBirth(dateOfBirth)
            .email(email)
            .provider(authProvider)
            .role(role)
            .build();

        // When
        BlogUser savedUser = blogUserRepository.save(user);

        // Then
        assertNotNull(savedUser);
        assertEquals(username, savedUser.getUsername());
        assertEquals(email, savedUser.getEmail());
        assertEquals(role, savedUser.getRole());
        assertEquals(authProvider, savedUser.getProvider());
        assertFalse(savedUser.isDeleted());
        assertTrue(savedUser.getCreatedAt().isBefore(LocalDateTime.now()));
        assertTrue(savedUser.getDateOfBirth().isBefore(LocalDate.now()));

    }



    @DisplayName("Find User with unique Username")
    @Test
    public void testFindByUsername(){

        // Given
        String username = "example123";
        createLocalUser(blogUserRepository, username);

        // When
        boolean haveUser = blogUserRepository.existsByUsername(username);
        Optional<BlogUser> optionalBlogUser = blogUserRepository.findByUsername(username);

        // Then
        assertTrue(haveUser);
        assertTrue(optionalBlogUser.isPresent());
        assertEquals(username, optionalBlogUser.get().getUsername());
    }



    @DisplayName("Find User with Provider and Subject")
    @Test
    public void testFindByProviderAndSubject(){

        // Given
        AuthProvider provider = AuthProvider.GOOGLE;
        String subject = UUID.randomUUID().toString().replace("-", "");
        createOAuth2User(blogUserRepository, provider, subject);

        // When
        Optional<BlogUser> optionalBlogUser = blogUserRepository.findByProviderAndSubject(provider, subject);


        // then
        assertTrue(optionalBlogUser.isPresent());
        assertEquals(provider, optionalBlogUser.get().getProvider());
        assertEquals(subject, optionalBlogUser.get().getSubject());

    }
}