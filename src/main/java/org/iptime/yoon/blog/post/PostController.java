package org.iptime.yoon.blog.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.common.dto.CreatedResourceIdResponse;
import org.iptime.yoon.blog.post.dto.*;
import org.iptime.yoon.blog.post.service.PostService;
import org.iptime.yoon.blog.security.CurrentUsername;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.iptime.yoon.blog.category.CategoryUtils.parseCategoryString;
import static org.iptime.yoon.blog.common.dto.ErrorResponse.createErrorResponse;

/**
 * @author rival
 * @since 2023-08-12
 */


@RestController
@RequestMapping("/api/posts")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PostController {


    private final PostService postService;
    public static final int SIZE_PER_PAGE = 10;


    // prev and next post
    @GetMapping("/prev-and-next")
    public PostPrevAndNextResponse getPrevAndNextPosts(@RequestParam(value = "postId") Long postId) {
        return postService.findPrevAndNextPosts(postId);
    }


    // CREATE
    @PostMapping("")
    public ResponseEntity<?> createPost(@AuthenticationPrincipal JwtUser jwtUser, @Valid @RequestBody PostCreateRequest postCreateRequest) {
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
    public PostPageResponse getPostPageByCategory(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @PathVariable String categoryString,
        @CurrentUsername String root) {
        PageRequest pageRequest = generatePageRequest(page);
        String sub = parseCategoryString(categoryString);
        log.info("Username : {}, Category : {}", root, sub);
        return postService.findPostListByCategory(pageRequest, root, sub);
    }

    @GetMapping("")
    public PostPageResponse getPostPage(@RequestParam(value = "page", defaultValue = "1") int page) {
        PageRequest pageRequest = generatePageRequest(page);
        return postService.findPostList(pageRequest);
    }


    private PageRequest generatePageRequest(int clientPageNum) {
        int zeroBasedPage = clientPageNum > 0 ? clientPageNum - 1 : 0;
        Sort sort = Sort.by("id").descending();
        return PageRequest.of(zeroBasedPage, SIZE_PER_PAGE, sort);
    }

    @GetMapping("/search")
    public PostPageResponse searchPostPage(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @ModelAttribute PostSearchQuery postSearchQuery) {

        PageRequest pageRequest = generatePageRequest(page);
        return postService.searchPostList(postSearchQuery, pageRequest);
    }


    // UPDATE
    // EntityNotFoundException handling
    @PutMapping("/{id}")
    @PreAuthorize("@postServiceImpl.isOwner(#id, authentication.name)")
    public ResponseEntity<?> updatePostById(@CurrentUsername String username,  @PathVariable(name = "id") Long id, @RequestBody PostCreateRequest postCreateRequest) {
        postService.updatePost(username, id, postCreateRequest);
        return ResponseEntity.ok().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("@postServiceImpl.isOwner(#id, authentication.name)")
    public ResponseEntity<?> deletePostById(@CurrentUsername String username, @PathVariable(name = "id") Long id) {
        postService.deletePost(username, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(value = PostEntityNotFoundException.class)
    public ResponseEntity<?> postNotFoundExceptionHandler(PostEntityNotFoundException e) {
        log.info("Post Not Found", e);
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
