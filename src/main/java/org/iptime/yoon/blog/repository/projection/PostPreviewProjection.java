package org.iptime.yoon.blog.repository.projection;

import java.time.LocalDateTime;

/**
 * @author rival
 * @since 2023-08-12
 */
public interface PostPreviewProjection {
    Long getId();
    String getTitle();
    String getWriterName();

    String getDescription();

    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
