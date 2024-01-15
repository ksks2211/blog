package org.iptime.yoon.blog.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.common.CreatedResourceIdResponse;
import org.iptime.yoon.blog.post.dto.PostCreateRequest;
import org.iptime.yoon.blog.post.dto.PostPageResponse;
import org.iptime.yoon.blog.post.dto.PostPrevAndNextResponse;
import org.iptime.yoon.blog.post.dto.PostResponse;
import org.iptime.yoon.blog.post.service.PostService;
import org.iptime.yoon.blog.security.CurrentUsername;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.iptime.yoon.blog.category.CategoryUtils.parseCategoryString;
import static org.iptime.yoon.blog.common.ErrorResponse.createErrorResponse;

/**
 * @author rival
 * @since 2023-08-12
 */


@RestController
@RequestMapping("/api/posts")
@Slf4j
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;
    public static int SIZE_PER_PAGE = 10;


    // prev and next post
    @GetMapping("/prev-and-next")
    public PostPrevAndNextResponse getPrevAndNextPosts(@RequestParam(value = "postId") Long postId) {
        return postService.findPrevAndNextPosts(postId);
    }


    // CREATE
    @PostMapping("")
    public ResponseEntity<?> createPost(@AuthenticationPrincipal JwtUser jwtUser, @RequestBody PostCreateRequest postCreateRequest) {
        Long id = postService.createPost(postCreateRequest, jwtUser);
        CreatedResourceIdResponse body = new CreatedResourceIdResponse(id);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }


    // READ
    // EntityNotFoundException handling
    @GetMapping("/{id}")
    public PostResponse getPostById(@PathVariable(name = "id") Long id) {
        return postService.findById(id);
    }


    @GetMapping("/categories/{categoryString}")
    public PostPageResponse getPostPageByCategory(@RequestParam(value = "page", defaultValue = "1") int page, @PathVariable String categoryString, @CurrentUsername String root) {
        int zeroBasedPage = page > 0 ? page - 1 : 0;
        String sub = parseCategoryString(categoryString);
        log.info("Username : {}, Category : {}", root, sub);
        Sort sort = Sort.by("id").descending();
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, SIZE_PER_PAGE, sort);
        return postService.findPostListByCategory(pageRequest, root, sub);
    }

    @GetMapping("")
    public PostPageResponse getPostPage(@RequestParam(value = "page", defaultValue = "1") int page) {
        int zeroBasedPage = page > 0 ? page - 1 : 0;
        Sort sort = Sort.by("id").descending();
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, SIZE_PER_PAGE, sort);
        return postService.findPostList(pageRequest);
    }




    // UPDATE
    // EntityNotFoundException handling
    @PutMapping("/{id}")
    @PreAuthorize("@postServiceBean.isOwner(#id, authentication.name)")
    public PostResponse updatePostById(@PathVariable(name = "id") Long id, @RequestBody PostCreateRequest postCreateRequest) {
        return postService.updatePost(id, postCreateRequest);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("@postServiceBean.isOwner(#id, authentication.name)")
    public ResponseEntity<?> deletePostById(@PathVariable(name = "id") Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(value = PostEntityNotFoundException.class)
    public ResponseEntity<?> postNotFoundExceptionHandler(PostEntityNotFoundException e) {
        log.info(e.getClass().getName());
        log.info(e.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
