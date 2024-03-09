package org.iptime.yoon.blog.image;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

/**
 * @author rival
 * @since 2024-03-03
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class ImageResourceDto {
    private Long id;
    private String originalName;
    private Resource resource;
    private String contentType;
    private Long size;
    private String createdDate;
}
