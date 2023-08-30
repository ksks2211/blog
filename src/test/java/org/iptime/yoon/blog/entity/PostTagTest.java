package org.iptime.yoon.blog.entity;

import org.iptime.yoon.blog.config.JpaConfig;
import org.iptime.yoon.blog.repository.PostTagRepository;
import org.iptime.yoon.blog.security.entity.BlogUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rival
 * @since 2023-08-30
 */

@DataJpaTest
@Import(JpaConfig.class)
class PostTagTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private PostTagRepository postTagRepository;

    public BlogUser createBlogUser(String email, String password){
        BlogUser user = BlogUser.builder()
            .username(email)
            .password(password)
            .build();
        em.persist(user);
        return user;
    }

    public Post createPost(BlogUser blogUser,String title,String content){
        Post post = Post.builder()
            .writer(blogUser)
            .title(title)
            .content(content)
            .build();
        em.persist(post);
        return post;
    }


    public Tag createTag(String keyword){
        Tag tag = new Tag();
        tag.setTag(keyword);
        em.persist(tag);
        return tag;
    }



    @Test
    public void testSaveAndFind(){

        // Given
        String email ="email@email.com";
        String password="password";
        String keyword = "key";
        BlogUser user = createBlogUser(email,password);
        Post post = createPost(user,"title","content");
        Tag tag = createTag(keyword);


        // when
        PostTag postTag = PostTag.builder()
            .post(post)
            .tag(tag)
            .build();
        postTagRepository.save(postTag);
        List<PostTag> postTagList = postTagRepository.findAllByTag(keyword);


        assertThat(postTagList).isNotNull();
        assertThat(postTagList.size()).isEqualTo(1);

    }

}