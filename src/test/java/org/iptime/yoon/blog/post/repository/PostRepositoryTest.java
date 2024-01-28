package org.iptime.yoon.blog.post.repository;

import org.iptime.yoon.blog.category.Category;
import org.iptime.yoon.blog.category.CategoryRepository;
import org.iptime.yoon.blog.common.config.JpaConfig;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.entity.PostTag;
import org.iptime.yoon.blog.post.entity.Tag;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewProjection;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.iptime.yoon.blog.user.repository.BlogUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.iptime.yoon.blog.category.CategoryRepositoryTest.createCategory;
import static org.iptime.yoon.blog.post.repository.PostTestHelper.createPost;
import static org.iptime.yoon.blog.post.repository.PostTestHelper.createPostWithCategory;
import static org.iptime.yoon.blog.user.repository.BlogUserTestHelper.createLocalUser;
import static org.junit.jupiter.api.Assertions.*;

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

    @Autowired
    private CategoryRepository categoryRepository;


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
    void findNextAndPrevPost() {
        // Given
        String username = "example123";
        String prev = "Title 1";
        String t2 = "Title 2";
        String next = "Title 3";
        BlogUser user = createLocalUser(blogUserRepository, username);
        createPost(postRepository, user, prev);
        Long middle = createPost(postRepository, user, t2).getId();
        createPost(postRepository, user, next);


        // when
        Optional<PostPreviewProjection> prevPost = postRepository.findPrevPost(middle);
        Optional<PostPreviewProjection> nextPost = postRepository.findNextPost(middle);


        assertTrue(prevPost.isPresent());
        assertTrue(nextPost.isPresent());

        prevPost.ifPresent(p-> assertEquals(prev,p.getTitle()));
        nextPost.ifPresent(n-> assertEquals(next,n.getTitle()));

    }


    @Test
    void findPostListByCategory() {

        // Given
        String username = "example123";
        String fullName = "example123/dir/dir2/dir3";
        BlogUser user = createLocalUser(blogUserRepository, username);
        Category category = createCategory(categoryRepository, username, fullName);

        String t1 = "Hello World";
        String t2 = "Nice to meet U";
        createPostWithCategory(postRepository, user,t1,category);
        createPostWithCategory(postRepository, user,t2,category);

        // When
        Page<PostPreviewProjection> page = postRepository.findPostListByCategory(Pageable.ofSize(10), category);
        List<PostPreviewProjection> posts = page.getContent();
        List<String> titles = posts.stream().map(PostPreviewProjection::getTitle).toList();


        assertEquals(2, posts.size());
        assertTrue(titles.contains(t1));
        assertTrue(titles.contains(t2));
    }
}