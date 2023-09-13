package org.iptime.yoon.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.entity.ImageMetadata;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

/**
 * @author rival
 * @since 2023-08-13
 */

@Data
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ImageFileDto implements Serializable {


    private byte[] bytes;
    private String contentType;
    private Long id;
    private Long size;
    private String originalName;
    private String createdDate;

    public static ImageFileDto createImageFile(ImageMetadata metadata, byte[] bytes){
        return ImageFileDto.builder()
            .id(metadata.getId())
            .bytes(bytes)
            .contentType(metadata.getContentType())
            .size(metadata.getSize())
            .createdDate(metadata.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME))
            .originalName(metadata.getOriginalName())
            .build();
    }
}
