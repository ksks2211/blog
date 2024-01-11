package org.iptime.yoon.blog.post.repository;

import org.iptime.yoon.blog.post.entity.PostBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rival
 * @since 2023-08-17
 */


public interface BookmarkRepository extends JpaRepository<PostBookmark,Long> {
}
