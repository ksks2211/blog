package org.iptime.yoon.blog.image;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

/**
 * @author rival
 * @since 2023-08-13
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class ImageDto implements Serializable {

    private byte[] bytes;
    private String contentType;
    private Long id;
    private Long size;
    private String originalName;
    private String createdDate;

    public static ImageDto fromEntity(Image entity, byte[] bytes){
        return ImageDto.builder()
            .id(entity.getId())
            .bytes(bytes)
            .contentType(entity.getContentType())
            .size(entity.getSize())
            .createdDate(entity.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME))
            .originalName(entity.getOriginalName())
            .build();
    }
}
