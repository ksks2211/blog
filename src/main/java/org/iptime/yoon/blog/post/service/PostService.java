package org.iptime.yoon.blog.post.service;

import org.iptime.yoon.blog.post.dto.*;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author rival
 * @since 2023-08-11
 */

public interface PostService {


    // Create
    Long createPost(PostCreateRequest createReqDto, JwtUser jwtUser);

    // Read
    PostResponse findById(Long id);


    PostPageResponse findPostList(Pageable pageable);
    PostPageResponse findPostListByCategory(Pageable pageable, String root, String sub);

    PostPrevAndNextResponse findPrevAndNextPosts(Long id);


    // Update
    PostResponse updatePost(Long id, PostCreateRequest createReqDto);


    // Delete
    void deletePost(Long id);


    boolean isOwner(Long id, String username);

    PostPageResponse searchPostList(PostSearchQuery postSearchQuery, PageRequest pageRequest);
}
