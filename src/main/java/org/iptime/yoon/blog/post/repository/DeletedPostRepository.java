package org.iptime.yoon.blog.post.repository;

import org.iptime.yoon.blog.post.entity.DeletedPost;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rival
 * @since 2024-01-21
 */
public interface DeletedPostRepository extends JpaRepository<DeletedPost, Long> {
}
