package org.iptime.yoon.blog.dto.res;

import lombok.Data;
import org.iptime.yoon.blog.entity.Post;
import org.iptime.yoon.blog.repository.projection.PostPreview;

import java.time.LocalDateTime;

/**
 * @author rival
 * @since 2023-08-12
 */
@Data
public class PostPreviewDto {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String writerEmail;

    public static PostPreviewDto fromPostPreview(PostPreview post){
        PostPreviewDto dto = new PostPreviewDto();
        dto.setId(post.getId());
        dto.setWriterEmail(post.getWriterEmail());
        dto.setTitle(post.getTitle());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
}
