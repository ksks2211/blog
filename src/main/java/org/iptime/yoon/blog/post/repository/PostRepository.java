package org.iptime.yoon.blog.post.repository;

import org.iptime.yoon.blog.category.Category;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author rival
 * @since 2023-08-10
 */
public interface PostRepository extends JpaRepository<Post,Long>, JpaSpecificationExecutor<Post>, PostSearchRepository {

    Page<PostPreviewProjection> findProjectedBy(Pageable pageable);


    @Query("select p.id as id, p.title as title, p.writerName as writerName, p.createdAt as createdAt, p.updatedAt as updatedAt, p.description as description " +
        "from Post p " +
        "where p.id > :id " +
        "order by p.id asc limit 1")
    Optional<PostPreviewProjection> findNextPost(Long id);

    @Query("select p.id as id, p.title as title, p.writerName as writerName,p.writerDisplayName as writerDisplayName, p.createdAt as createdAt, p.updatedAt as updatedAt, p.description as description " +
        "from Post p " +
        "where p.id < :id " +
        "order by p.id desc limit 1")
    Optional<PostPreviewProjection> findPrevPost(Long id);

    @Query("select p.id as id, p.title as title, p.writerName as writerName,p.writerDisplayName as writerDisplayName, p.createdAt as createdAt, p.updatedAt as updatedAt, p.description as description " +
        "from Post p "+
        "where p.category = :category")
    Page<PostPreviewProjection> findPostListByCategory(Pageable pageable, Category category);



    @Query("select p.category.fullName from Post p where p.id = :id")
    Optional<String> findCategoryFullNameById(Long id);

}
