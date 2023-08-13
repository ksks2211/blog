package org.iptime.yoon.blog.dto.req;

import lombok.Data;
import org.iptime.yoon.blog.entity.Post;

/**
 * @author rival
 * @since 2023-08-11
 */

@Data
public class PostReqDto {
    private String title;
    private String content;

    public Post toEntity(){
        return Post.builder().title(title).content(content).build();
    }
}
