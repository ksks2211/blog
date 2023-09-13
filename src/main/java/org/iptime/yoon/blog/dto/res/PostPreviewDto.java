package org.iptime.yoon.blog.dto.res;

import lombok.Data;
import org.iptime.yoon.blog.repository.projection.PostPreviewProjection;

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
    private String writer;

    public static PostPreviewDto fromPostPreview(PostPreviewProjection post){
        PostPreviewDto dto = new PostPreviewDto();
        dto.setId(post.getId());
        dto.setWriter(post.getWriterName());
        dto.setTitle(post.getTitle());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
}
