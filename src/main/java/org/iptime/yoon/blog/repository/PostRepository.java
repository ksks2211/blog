package org.iptime.yoon.blog.repository;

import org.iptime.yoon.blog.entity.Post;
import org.iptime.yoon.blog.repository.projection.PostPreviewProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author rival
 * @since 2023-08-10
 */
public interface PostRepository extends JpaRepository<Post,Long> {
    @Query("select p.id as id, p.title as title, p.writerEmail as writerEmail, p.createdAt as createdAt, p.updatedAt as updatedAt from Post p")
    Page<PostPreviewProjection> findPostList(Pageable pageable);

}
