package org.iptime.yoon.blog.post.dto;

import lombok.Data;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.user.entity.BlogUser;

import java.util.HashSet;
import java.util.Set;

/**
 * @author rival
 * @since 2023-08-11
 */

@Data
public class PostCreateRequest {
    private String title;
    private String content;
    private String description;

    private Set<String> tags = new HashSet<>();


    // "/dir", "/dir1/dir2", "/dir1/dir2/dir3"
    private String category = "/";

    public Post toEntity(Long id,String username){
        return Post.builder()
            .title(title)
            .content(content)
            .description(description)
            .writer(BlogUser.builder().id(id).build())
            .writerName(username).build();
    }
}
