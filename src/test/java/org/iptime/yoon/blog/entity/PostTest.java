package org.iptime.yoon.blog.entity;

import org.iptime.yoon.blog.category.Category;
import org.iptime.yoon.blog.common.config.JpaConfig;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.repository.PostRepository;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewProjection;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rival
 * @since 2023-08-10
 */


@DataJpaTest
@Import(JpaConfig.class)
class PostTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    public BlogUser createBlogUser(String email, String password){
        BlogUser user = BlogUser.builder()
            .username(email)
            .password(password)
            .build();

        entityManager.persist(user);
        return user;
    }

    public Post createPost(BlogUser writer, String title, String content){
        Post post = Post.builder()
            .writer(writer)
            .title(title)
            .content(content)
            .build();
        entityManager.persist(post);
        return post;
    }


    public Post createPostWithCategory(BlogUser writer, String title, String content, Category category){
        Post post = Post.builder()
            .writer(writer)
            .title(title)
            .content(content)
            .category(category)
            .build();
        entityManager.persist(post);
        return post;
    }

    public Category createCategory(String fullName){
        Category category = Category.builder().root("ADMIN").fullName(fullName).build();
        entityManager.persist(category);
        return category;
    }


    @Test
    public void testFindingPrevAndNext(){

        // Given
        String email="email@email.com";
        String password="12341234";
        BlogUser writer = createBlogUser(email, password);

        IntStream.range(0,5).forEach(i->{
            String title = "post title " + i;
            String content = "post content" + i;
            createPost(writer, title, content);
        });

        List<Post> posts = postRepository.findAll();

        Post post = posts.get(2);
        PostPreviewProjection prev = postRepository.findPrevPost(post.getId()).get();
        PostPreviewProjection next = postRepository.findNextPost(post.getId()).get();

        assertThat(post.getId()).isGreaterThan(prev.getId());
        assertThat(post.getId()).isLessThan(next.getId());
    }

    @Test
    public void testSaveAndFindPost(){
        // Given
        String email="email@email.com";
        String password="12341234";
        BlogUser writer = createBlogUser(email, password);

        String title = "post title";
        String content = "post content";
        Post post = createPost(writer, title, content);


        // When
        Post foundPost = postRepository.findById(post.getId()).orElse(null);

        // Then
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getTitle()).isEqualTo(title);
        assertThat(foundPost.getContent()).isEqualTo(content);
        assertThat(foundPost.getId()).isEqualTo(post.getId());
        assertThat(foundPost.getWriter().getUsername()).isEqualTo(email);

        logger.info("foundPost : {}",foundPost);
    }

    @Test
    public void testSoftDeletePost(){
        // Given
        String email="email@email.com";
        String password="12341234";
        BlogUser writer = createBlogUser(email, password);

        String title = "post title";
        String content = "post content";
        Post post = createPost(writer, title, content);



        post.softDelete();

        postRepository.save(post);

        int size = postRepository.findAll().size();

        assertThat(size).isEqualTo(0);
    }

    @Test
    public void testCreatePostsAndFindPosts(){
        BlogUser writer = createBlogUser("example@email.com", "12341234");

        int numOfPosts = 30;
        IntStream.range(0,numOfPosts).forEach(i->{
            Post post = Post.builder().content("Content " + i).title("Title " + i).writer(writer).build();
            postRepository.save(post);
        });


        List<Post> posts = postRepository.findAll();

        assertThat(posts.size()).isEqualTo(numOfPosts);

        posts.forEach(post->logger.info("{}",post));
    }


    @Test
    public void getPostByCategory(){
        // Given
        String email ="email@email.com";
        String password="password";
        BlogUser user = createBlogUser(email,password);
        Category category = createCategory("ADMIN/HELLO/WORLD");

        String title = "Hello World";
        createPostWithCategory(user,title,"content 123125",category);

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<PostPreviewProjection> result = postRepository.findPostListByCategory(pageRequest, category);


        assertThat(result.getTotalElements()).isEqualTo(1L);
        assertThat(result.getTotalPages()).isEqualTo(1);

        assertThat(result.getContent().get(0).getTitle()).isEqualTo(title);


    }

    @Test
    public void testCreatePostsAndFindPostsWithPaging(){
        int numOfPosts = 30;
        int numOfPage = 0;
        int numOfPageSize = 10;

        BlogUser writer = createBlogUser("example@email.com", "12341234");

        List<Post> posts = new ArrayList<>();
        IntStream.range(0,numOfPosts).forEach(i->{
            Post post = Post.builder().content("Content " + i).title("Title " + i).writer(writer).writerName(writer.getUsername()).build();
            posts.add(post);
        });
        postRepository.saveAll(posts);


        PageRequest pageRequest = PageRequest.of(numOfPage, numOfPageSize);
        Page<PostPreviewProjection> page = postRepository.findPostList(pageRequest);

        assertThat(page.getTotalElements()).isEqualTo(numOfPosts);
        assertThat(page.getTotalPages()).isEqualTo(numOfPosts/numOfPageSize);
        assertThat(page.getContent().size()).isEqualTo(numOfPageSize);

        page.getContent().forEach(post->logger.info("Id : {}, Title : {}",post.getId(), post.getTitle()));
    }
}