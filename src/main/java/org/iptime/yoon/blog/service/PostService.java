package org.iptime.yoon.blog.service;

import org.iptime.yoon.blog.dto.req.PostReqDto;
import org.iptime.yoon.blog.dto.res.PostPageResDto;
import org.iptime.yoon.blog.dto.res.PostResDto;
import org.springframework.data.domain.Pageable;

/**
 * @author rival
 * @since 2023-08-11
 */

public interface PostService {


    // Create
    PostResDto create(PostReqDto createReqDto);

    // Read
    PostResDto findById(Long id);


    PostPageResDto findPostList(Pageable pageable);


    // Update
    PostResDto update(Long id, PostReqDto createReqDto);


    // Delete
    void delete(Long id);


}
