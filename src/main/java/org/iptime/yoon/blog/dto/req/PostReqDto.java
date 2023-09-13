package org.iptime.yoon.blog.dto.req;

import lombok.Data;
import org.iptime.yoon.blog.entity.Post;
import org.iptime.yoon.blog.security.entity.BlogUser;

import java.util.HashSet;
import java.util.Set;

/**
 * @author rival
 * @since 2023-08-11
 */

@Data
public class PostReqDto {
    private String title;
    private String content;

    private Set<String> tags = new HashSet<>();


    // "dir1/dir2
    private String category = "";

    public Post toEntity(Long id,String username){
        return Post.builder()
            .title(title)
            .content(content)
            .writer(BlogUser.builder().id(id).build())
            .writerName(username).build();
    }
}
