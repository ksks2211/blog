package org.iptime.yoon.blog.post;

import org.iptime.yoon.blog.category.Category;
import org.iptime.yoon.blog.post.dto.PostCreateRequest;
import org.iptime.yoon.blog.post.dto.PostPreviewResponse;
import org.iptime.yoon.blog.post.dto.PostResponse;
import org.iptime.yoon.blog.post.entity.DeletedPost;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.entity.Tag;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewDto;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewProjection;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rival
 * @since 2024-02-20
 */
class PostMapperTest {

    private PostMapper postMapper;

    @BeforeEach
    public void init(){
        postMapper = Mappers.getMapper(PostMapper.class);
    }


    @Test
    public void postToPostResponse(){
        Post post = Post.builder()
            .content("hello world")
            .id(1L)
            .title("Title of post")
            .description("description of post")
            .writerName("writerA")
            .writerDisplayName("writer D")
            .build();
        post.setCreatedAt(LocalDateTime.now().minusDays(10));
        post.setUpdatedAt(LocalDateTime.now());

        List<String> tags = List.of("hello","world");
        String category = "sdasdsa/greetings";
        PostResponse postResponse = postMapper.postToPostResponse(post, tags, category);


        assertNotNull(postResponse);
        assertEquals(post.getId(), postResponse.getId());
        assertEquals(post.getTitle(), postResponse.getTitle());
        assertEquals(post.getWriterName(), postResponse.getWriter());
        assertEquals(post.getWriterDisplayName(),postResponse.getWriterDisplayName());
        assertEquals(post.getContent(), postResponse.getContent());
        assertEquals(post.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME), postResponse.getCreatedAt());
        assertEquals(post.getUpdatedAt().format(DateTimeFormatter.ISO_DATE_TIME), postResponse.getUpdatedAt());

        assertEquals(tags.size(), postResponse.getTags().size());
        assertEquals(category, postResponse.getCategory());
    }


    @Test
    public void postProjToPostRes(){
        PostPreviewProjection post = PostPreviewDto.builder()
            .id(1L)
            .title("title")
            .writerName("name")
            .writerDisplayName("dname")
            .description("desc")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();


        PostPreviewResponse postResponse = postMapper.postProjToPostRes(post);

        assertNotNull(postResponse);
        assertEquals(post.getId(), postResponse.getId());
        assertEquals(post.getTitle(), postResponse.getTitle());
        assertEquals(post.getWriterName(), postResponse.getWriter());
        assertEquals(post.getWriterDisplayName(),postResponse.getWriterDisplayName());
        assertEquals(post.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME), postResponse.getCreatedAt());
        assertEquals(post.getUpdatedAt().format(DateTimeFormatter.ISO_DATE_TIME), postResponse.getUpdatedAt());
    }


    @Test
    public void postDtoToPostRes(){
        PostPreviewDto post = PostPreviewDto.builder()
            .id(1L)
            .title("title")
            .writerName("name")
            .writerDisplayName("dname")
            .description("desc")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        PostPreviewResponse postResponse = postMapper.postDtoToPostRes(post);

        assertNotNull(postResponse);
        assertEquals(post.getId(), postResponse.getId());
        assertEquals(post.getTitle(), postResponse.getTitle());
        assertEquals(post.getWriterName(), postResponse.getWriter());
        assertEquals(post.getWriterDisplayName(),postResponse.getWriterDisplayName());
        assertEquals(post.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME), postResponse.getCreatedAt());
        assertEquals(post.getUpdatedAt().format(DateTimeFormatter.ISO_DATE_TIME), postResponse.getUpdatedAt());
    }





    @Test
    public void postToDeletedPost(){

        Post post = Post.builder()
            .content("hello world")
            .id(1L)
            .title("Title of post")
            .description("description of post")
            .writerName("writerA")
            .writerDisplayName("writer D")
            .build();
        post.setCreatedAt(LocalDateTime.now().minusDays(10));
        post.setUpdatedAt(LocalDateTime.now());

        List<String> tags = List.of("hello","world");
        String category = "/greetings";

        DeletedPost deletedPost = postMapper.postToDeletedPost(post, category, tags);

        assertNotNull(deletedPost);
        assertEquals(post.getId(), deletedPost.getPostId());
        assertEquals(post.getTitle(), deletedPost.getTitle());
        assertEquals(post.getWriterDisplayName(),deletedPost.getWriterDisplayName());
        assertEquals(post.getContent(), deletedPost.getContent());
        assertTrue(post.getCreatedAt().isBefore(deletedPost.getDeletedAt()));
        assertEquals(tags.size(), deletedPost.getTags().size());
        assertEquals(category, deletedPost.getCategory());
    }


    @Test
    public void postCreateRequestToPost(){

        var req = new PostCreateRequest();
        req.setTitle("Title");
        req.setCategory("Cate");
        req.setContent("content");
        req.setDescription("Desc");
        req.getTags().add("java");
        req.getTags().add("spring");

        var writer = JwtUser.builder()
            .username("user123")
            .displayName("user1232132")
            .id(123L)
            .build();

        var category = Category.builder().root("userA").fullName("userA/dir1/dir2").id(43L).build();

        var tags = req.getTags().stream().map(Tag::new).toList();

        Post post = postMapper.postCreateRequestToPost(req, writer, category, tags);

        assertEquals(req.getTags().size(),post.getPostTags().size());
        assertEquals(req.getContent(), post.getContent());
        assertEquals(req.getDescription(), post.getDescription());
        assertEquals(category.getFullName(), post.getCategory().getFullName());
        assertNull(post.getId());
    }
}