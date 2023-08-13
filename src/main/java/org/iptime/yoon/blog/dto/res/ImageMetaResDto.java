package org.iptime.yoon.blog.dto.res;

import lombok.Builder;
import lombok.Data;
import org.iptime.yoon.blog.entity.ImageMetadata;

import java.time.LocalDateTime;

/**
 * @author rival
 * @since 2023-08-13
 */

@Data
@Builder
public class ImageMetaResDto {
    private String contentType;
    private Long id;
    private Long size;
    private String originalName;
    private LocalDateTime createdAt;

    public static ImageMetaResDto createImageMetaDto(ImageMetadata metadata){
        return ImageMetaResDto.builder()
            .id(metadata.getId())
            .size(metadata.getSize())
            .contentType(metadata.getContentType())
            .originalName(metadata.getOriginalName())
            .createdAt(metadata.getCreatedAt())
            .build();
    }
}
