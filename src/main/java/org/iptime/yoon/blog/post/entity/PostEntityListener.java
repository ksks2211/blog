package org.iptime.yoon.blog.post.entity;

import jakarta.persistence.PreRemove;
import org.iptime.yoon.blog.common.BeanUtil;
import org.iptime.yoon.blog.post.PostMapper;
import org.iptime.yoon.blog.post.repository.DeletedPostRepository;
import org.iptime.yoon.blog.post.repository.PostRepository;
import org.iptime.yoon.blog.user.entity.BlogUser;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rival
 * @since 2024-01-21
 */
public class PostEntityListener {

    @PreRemove
    public void onPreRemove(Post post){

        PostMapper postMapper = BeanUtil.getBean(PostMapper.class);

        DeletedPost deletedPost = postMapper.postToDeletedPost(post);
        deletedPost.setDeletedAt(LocalDateTime.now());

        BlogUser writer = post.getWriter();
        deletedPost.setWriterId(writer.getId());

        String category = post.getCategory().getFullName();
        deletedPost.setCategory(category);

        List<String> tags = post.getPostTags().stream().map(tag -> tag.getTag().getValue()).toList();
        deletedPost.setTags(tags);

        DeletedPostRepository deletedPostRepository = BeanUtil.getBean(DeletedPostRepository.class);
        deletedPostRepository.save(deletedPost);


        post.setCategory(null);
        post.removeAllPostTags();

        PostRepository postRepository = BeanUtil.getBean(PostRepository.class);
        postRepository.save(post);

    }
}
