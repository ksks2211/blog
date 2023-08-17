package org.iptime.yoon.blog.entity;

import org.iptime.yoon.blog.config.JpaConfig;
import org.iptime.yoon.blog.security.entity.BlogUser;
import org.iptime.yoon.blog.security.entity.RefreshToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rival
 * @since 2023-08-17
 */

@DataJpaTest
@Import(JpaConfig.class)
class RefreshTokenTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test void createRefreshToken(){
        BlogUser user = BlogUser.builder()
            .username("username")
            .password("password")
            .build();

        entityManager.persist(user);

        RefreshToken token = RefreshToken.builder().user(user).build();
        entityManager.persist(token);

        String tokenStr = token.getValue();
        assertThat(tokenStr).isNotNull();


    }

}