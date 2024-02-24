package org.iptime.yoon.blog.post.repository.projection;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author rival
 * @since 2024-01-20
 */


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPreviewDto implements PostPreviewProjection{

    Long id;
    String title;

    String writerName;
    String writerDisplayName;

    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
