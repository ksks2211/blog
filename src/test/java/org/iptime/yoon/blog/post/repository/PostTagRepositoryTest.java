package org.iptime.yoon.blog.post.repository;

import org.iptime.yoon.blog.common.config.JpaConfig;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.iptime.yoon.blog.post.repository.PostTestHelper.createPostWithTags;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rival
 * @since 2024-02-20
 */

@DataJpaTest
@Import(JpaConfig.class)
class PostTagRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Test
    void findAllTagsByPostId() {
        List<String> tagList = List.of("hello","world");

        Post createdPost = createPostWithTags(postRepository, tagRepository, tagList);

        List<String> readTags = postTagRepository.findAllTagsByPostId(createdPost.getId());

        readTags.forEach(tag-> assertTrue(tagList.contains(tag)));
    }


    @Test
    void findByTagValueTest(){
        String value = "example";
        Tag tag = new Tag(value);
        tagRepository.save(tag);

        Optional<Tag> optional = tagRepository.findByValueIgnoreCase(value);
        assertTrue(optional.isPresent());
        assertNotNull(optional.get());
        assertEquals(value,optional.get().getValue());
    }
}