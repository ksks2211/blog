package org.iptime.yoon.blog.post.repository;

import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author rival
 * @since 2024-01-20
 */
public interface PostSearchRepository {


    Page<PostPreviewDto> searchAllPosts(Specification<Post> spec, Pageable pageable);
}
