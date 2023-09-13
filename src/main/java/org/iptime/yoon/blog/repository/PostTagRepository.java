package org.iptime.yoon.blog.repository;

import org.iptime.yoon.blog.entity.Post;
import org.iptime.yoon.blog.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author rival
 * @since 2023-08-30
 */
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    @Query("select pt from PostTag pt where pt.tag.tag = :tag")
    List<PostTag> findAllByTag(String tag);


    @Query("select pt.tag.tag from PostTag pt where pt.post.id = :id")
    List<String> findAllTagsByPostId(Long id);


    @Modifying
    void deleteAllByPost(Post post);
}
