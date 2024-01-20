package org.iptime.yoon.blog.post.repository.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rival
 * @since 2024-01-20
 */
@Data
@AllArgsConstructor
public class PostPreviewDto implements PostPreviewProjection{

    Long id;
    String title;

    String writerName;

    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
