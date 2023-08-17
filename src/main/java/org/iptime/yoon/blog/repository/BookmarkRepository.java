package org.iptime.yoon.blog.repository;

import org.iptime.yoon.blog.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rival
 * @since 2023-08-17
 */


public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
}
