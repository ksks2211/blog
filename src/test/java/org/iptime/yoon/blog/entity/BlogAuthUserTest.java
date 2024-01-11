package org.iptime.yoon.blog.entity;

import org.iptime.yoon.blog.common.config.JpaConfig;
import org.iptime.yoon.blog.user.repository.BlogUserRepository;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rival
 * @since 2023-08-11
 */

@DataJpaTest
@Import(JpaConfig.class)
class BlogAuthUserTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BlogUserRepository blogUserRepository;

    public BlogUser createBlogUser(String email, String password){
        BlogUser user = BlogUser.builder()
            .username(email)
            .password(password)
            .build();

        entityManager.persist(user);
        return user;
    }

    @Test
    public void testSaveAndFindBlogUser(){
        String email = "email@email.com";
        String password = "12345";
        BlogUser blogUser = createBlogUser(email, password);


        BlogUser foundBlogUser = blogUserRepository.findById(blogUser.getId()).orElse(null);


        assertThat(foundBlogUser).isNotNull();
        assertThat(foundBlogUser.getUsername()).isEqualTo(email);
        assertThat(foundBlogUser.getPassword()).isEqualTo(password);


        logger.info("foundBlogUser : {}",foundBlogUser);
    }
}