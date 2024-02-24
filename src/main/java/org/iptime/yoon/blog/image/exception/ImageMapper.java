package org.iptime.yoon.blog.image.exception;

import org.iptime.yoon.blog.image.Image;
import org.iptime.yoon.blog.image.ImageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author rival
 * @since 2024-02-20
 */

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "createdDate", source = "createdAt", qualifiedByName = "toISODateTime")
    @Mapping(target = "bytes", ignore = true)
    ImageDto imageToImageDto(Image image);


    @Named("toISODateTime")
    default String toISODateTime(LocalDateTime createdAt) {
        return createdAt.format(DateTimeFormatter.ISO_DATE_TIME);
    }


    default ImageDto imageToImageDto(Image image, byte[] data) {
        ImageDto imageDto = imageToImageDto(image);
        imageDto.setBytes(data);
        return imageDto;
    }

}
