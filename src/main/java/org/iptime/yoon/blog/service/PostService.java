package org.iptime.yoon.blog.service;

import org.iptime.yoon.blog.dto.req.PostReqDto;
import org.iptime.yoon.blog.dto.res.PostPageResDto;
import org.iptime.yoon.blog.dto.res.PostPrevAndNextResDto;
import org.iptime.yoon.blog.dto.res.PostResDto;
import org.iptime.yoon.blog.security.dto.User;
import org.springframework.data.domain.Pageable;

/**
 * @author rival
 * @since 2023-08-11
 */

public interface PostService {


    // Create
    PostResDto create(PostReqDto createReqDto, User user);

    // Read
    PostResDto findById(Long id);


    PostPageResDto findPostList(Pageable pageable);

    PostPrevAndNextResDto findPrevAndNextPosts(Long id);


    // Update
    PostResDto update(Long id, PostReqDto createReqDto);


    // Delete
    void delete(Long id);


}
