package org.iptime.yoon.blog.post.repository;

import org.iptime.yoon.blog.category.Category;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.entity.Tag;
import org.iptime.yoon.blog.user.entity.BlogUser;

import java.util.List;

/**
 * @author rival
 * @since 2024-01-28
 */
public class PostTestHelper {

    public static Post createPostWithCategory(PostRepository postRepository, BlogUser user, String title, Category category){

        String content = "This is the content";
        String description = "This is description";
        Post post = Post.builder()
            .title(title)
            .writer(user)
            .writerName(user.getDisplayName())
            .content(content)
            .description(description)
            .category(category)
            .build();
        return postRepository.save(post);
    }

    public static Post createPostWithTags(PostRepository postRepository,TagRepository tagRepository, List<String> tagList){
        String title = "This is the title";
        String content = "This is the content";
        String description = "This is description";

        List<Tag> tags = tagList.stream().map(Tag::new).toList();
        tagRepository.saveAll(tags);

        Post post = Post.builder()
            .title(title)
            .content(content)
            .description(description)
            .build();
        tags.forEach(post::addTag);
        return postRepository.save(post);
    }

    public static Post createPost(PostRepository postRepository, BlogUser blogUser, String title){
        String content = "This is the content";
        String description = "This is description";
        Post post = Post.builder()
            .title(title)
            .writer(blogUser)
            .writerName(blogUser.getDisplayName())
            .content(content)
            .description(description)
            .build();

        return postRepository.save(post);
    }
}
