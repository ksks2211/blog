package org.iptime.yoon.blog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.entity.ImageMetadata;

import java.time.LocalDateTime;

/**
 * @author rival
 * @since 2023-08-13
 */

@Data
@Builder
@Slf4j
public class ImageFileDto {

    private byte[] bytes;
    private String contentType;
    private Long id;
    private Long size;
    private String originalName;
    private LocalDateTime createdAt;

    public static ImageFileDto createImageFile(ImageMetadata metadata, byte[] bytes){
        return ImageFileDto.builder()
            .id(metadata.getId())
            .bytes(bytes)
            .contentType(metadata.getContentType())
            .size(metadata.getSize())
            .originalName(metadata.getOriginalName())
            .createdAt(metadata.getCreatedAt())
            .build();

    }

}
