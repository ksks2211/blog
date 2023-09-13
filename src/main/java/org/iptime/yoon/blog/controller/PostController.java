package org.iptime.yoon.blog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.dto.req.PostReqDto;
import org.iptime.yoon.blog.dto.res.ErrorResDto;
import org.iptime.yoon.blog.dto.res.PostPageResDto;
import org.iptime.yoon.blog.dto.res.PostResDto;
import org.iptime.yoon.blog.exception.PostEntityNotFoundException;
import org.iptime.yoon.blog.security.dto.User;
import org.iptime.yoon.blog.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author rival
 * @since 2023-08-12
 */


@RestController
@RequestMapping("/posts")
@Slf4j
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;
    public static int SIZE_PER_PAGE = 10;

    // CREATE
    @PostMapping("")
    public ResponseEntity<?> createPost(@AuthenticationPrincipal User user, @RequestBody PostReqDto postReqDto){
        PostResDto postResDto = postService.create(postReqDto, user);
        return new ResponseEntity<>(postResDto, HttpStatus.CREATED);
    }


    // READ
    // EntityNotFoundException handling
    @GetMapping("/{id}")
    public PostResDto getPostById(@PathVariable(name="id")Long id){
        return postService.findById(id);
    }


    @GetMapping("")
    public PostPageResDto getPostPage(@RequestParam(value="page", defaultValue = "1")int page){
        int zeroBasedPage = page > 0 ? page - 1 : 0;
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, SIZE_PER_PAGE);
        return postService.findPostList(pageRequest);
    }


    // UPDATE
    // EntityNotFoundException handling
    @PutMapping("/{id}")
    public PostResDto updatePostById(@PathVariable(name="id")Long id,@RequestBody PostReqDto postReqDto){
        return postService.update(id, postReqDto);
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable(name="id")Long id){
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(value= PostEntityNotFoundException.class)
    public ResponseEntity<?> postNotFoundExceptionHandler(PostEntityNotFoundException e){
        log.info(e.getClass().getName());
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResDto("Post Not Found", e.getMessage()));
    }

}
