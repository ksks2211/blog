package org.iptime.yoon.blog.user.repository;

import org.iptime.yoon.blog.user.entity.AuthProvider;
import org.iptime.yoon.blog.user.entity.BlogRole;
import org.iptime.yoon.blog.user.entity.BlogUser;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author rival
 * @since 2024-01-26
 */
public class BlogUserTestHelper {
    public static BlogUser createLocalUser(BlogUserRepository blogUserRepository, String username){
        String email = "example123@example.com";
        BlogRole role = BlogRole.USER;
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

        return blogUserRepository.save(user);
    }


    public static BlogUser createOAuth2User(BlogUserRepository blogUserRepository,AuthProvider provider, String subject){

        String randomUsername = UUID.randomUUID().toString().replace("-", "");

        String displayName = "profile name";


        String email = "example123@example.com";
        BlogRole role = BlogRole.USER;
        LocalDate dateOfBirth = LocalDate.now().minusYears(10);
        BlogUser user = BlogUser.builder()
            .username(randomUsername)
            .displayName(displayName)
            .dateOfBirth(dateOfBirth)
            .email(email)
            .provider(provider)
            .role(role)
            .subject(subject)
            .build();

        return blogUserRepository.save(user);
    }
}
