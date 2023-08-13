package org.iptime.yoon.blog.repository.projection;

import java.time.LocalDateTime;

/**
 * @author rival
 * @since 2023-08-12
 */
public interface PostPreview {
    Long getId();
    String getTitle();
    String getWriterEmail();

    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
