package org.iptime.yoon.blog.post.repository;

import org.iptime.yoon.blog.common.config.JpaConfig;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.entity.PostTag;
import org.iptime.yoon.blog.post.entity.Tag;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.iptime.yoon.blog.user.repository.BlogUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.iptime.yoon.blog.user.repository.BlogUserTestHelper.createLocalUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author rival
 * @since 2024-01-27
 */


@DataJpaTest
@Import(JpaConfig.class)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private BlogUserRepository blogUserRepository;


    @Test
    public void createPostWithTags() {
        // Given
        String username = "example123";
        BlogUser user = createLocalUser(blogUserRepository, username);
        String title = "Post Title";
        String content = "This is the content";
        String description = "This is description";
        String writerName = user.getDisplayName();
        List<String> tags = List.of("java", "spring");
        Post post = Post.builder().title(title).writer(user).writerName(writerName).content(content).description(description)
            .build();
        List<PostTag> postTags = tags.stream().map(tag -> {
                Tag t = tagRepository.save(new Tag(tag));
                return PostTag.builder().tag(t).post(post).build();
            }
        ).toList();
        post.setPostTags(postTags);


        // When
        Post savedPost = postRepository.save(post);

        // Then
        assertNotNull(savedPost);
        assertEquals(title, savedPost.getTitle());
        assertEquals(writerName, savedPost.getWriterName());
        assertEquals(tags.size(), savedPost.getPostTags().size());
    }

    @Test
    void findProjectedBy() {
    }

    @Test
    void findNextPost() {
    }

    @Test
    void findPrevPost() {
    }

    @Test
    void findPostListByCategory() {
    }
}