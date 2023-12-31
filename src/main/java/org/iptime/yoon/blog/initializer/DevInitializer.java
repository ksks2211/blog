package org.iptime.yoon.blog.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.dto.req.PostReqDto;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.service.CategoryService;
import org.iptime.yoon.blog.service.PostService;
import org.iptime.yoon.blog.user.dto.BlogUserRegisterRequest;
import org.iptime.yoon.blog.user.service.BlogUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author rival
 * @since 2023-08-31
 */

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DevInitializer implements ApplicationListener<ApplicationReadyEvent> {


    private final BlogUserService blogUserService;
    private final PostService postService;
    private final CategoryService categoryService;


    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        // Create ADMIN USER
        var req = new BlogUserRegisterRequest();
        req.setUsername("ADMIN");
        req.setPassword("12345");
        blogUserService.createBlogUser(req);
        log.info("ADMIN account registered");

        // Load BlogUser
        JwtUser admin = blogUserService.getJwtUserByUsername("ADMIN");

        // Create Posts
        IntStream.range(0,20).forEach(i->{
            PostReqDto post = new PostReqDto();
            post.setTitle("Title Of Post"+i);
            post.setContent("Content Of Post"+i);
            post.setCategory("/dir1/dir2");
            post.setDescription("Describe My Post ...... "+i);
            postService.createPost(post,admin);
        });
        PostReqDto taggedPost = getPostReqDto();

        Long id = postService.createPost(taggedPost, admin).getId();
        log.info("Post(Id = {}) with 3 tags created",id);


        // Create Empty Category
        categoryService.createCategoryIfNotExists("ADMIN","/empty/category");
    }

    @NotNull
    private static PostReqDto getPostReqDto() {
        PostReqDto taggedPost = new PostReqDto();
        taggedPost.setTitle("Title Of taggedPost");

        taggedPost.setContent("""
            ## Content Of taggedPost
            - Hello World!\s
            - Greetings
            ```js
            const a = 10;
            const b = a + 20;
            ```
            Here's a paragraph with a [link](https://www.example.com/).
            1. This is a numbered list item.
            2. This is another item
                        
            - [x] #739
            - [ ] https://github.com/octo-org/octo-repo/issues/740
            - [ ] Add delight to the experience when all tasks are complete :tada""");

        taggedPost.setTags(Set.of("java", "python", "javascript"));
        taggedPost.setCategory("/folder1/folder2");
        taggedPost.setDescription("Describe My Post ...... Tagged");
        return taggedPost;
    }
}
