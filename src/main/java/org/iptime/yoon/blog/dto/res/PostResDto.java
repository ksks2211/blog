package org.iptime.yoon.blog.dto.res;

import lombok.Data;
import org.iptime.yoon.blog.entity.Post;

import java.time.LocalDateTime;

/**
 * @author rival
 * @since 2023-08-11
 */
@Data
public class PostResDto {
    private Long id;
    private String title;
    private String content;
    private String writerEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResDto fromEntity(Post post){
        PostResDto postResDto = new PostResDto();

        postResDto.setId(post.getId());
        postResDto.setTitle(post.getTitle());
        postResDto.setContent(post.getContent());
        postResDto.setCreatedAt(post.getCreatedAt());
        postResDto.setUpdatedAt(post.getUpdatedAt());


        return postResDto;
    }
}
