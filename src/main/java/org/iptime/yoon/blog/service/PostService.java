package org.iptime.yoon.blog.service;

import org.iptime.yoon.blog.dto.req.PostReqDto;
import org.iptime.yoon.blog.dto.res.PostPageResDto;
import org.iptime.yoon.blog.dto.res.PostPrevAndNextResDto;
import org.iptime.yoon.blog.dto.res.PostResDto;
import org.iptime.yoon.blog.security.auth.AuthUser;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.springframework.data.domain.Pageable;

/**
 * @author rival
 * @since 2023-08-11
 */

public interface PostService {


    // Create
    PostResDto createPost(PostReqDto createReqDto, JwtUser jwtUser);

    // Read
    PostResDto findById(Long id);


    PostPageResDto findPostList(Pageable pageable);
    PostPageResDto findPostListByCategory(Pageable pageable,  String root, String sub);

    PostPrevAndNextResDto findPrevAndNextPosts(Long id);


    // Update
    PostResDto updatePost(Long id, PostReqDto createReqDto);


    // Delete
    void deletePost(Long id);


    boolean isOwner(Long id, String username);

}
