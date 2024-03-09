package org.iptime.yoon.blog.image.exception;

import org.iptime.yoon.blog.image.Image;
import org.iptime.yoon.blog.image.ImageDto;
import org.iptime.yoon.blog.image.ImageResourceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.core.io.Resource;

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



    @Mapping(target = "createdDate", source = "createdAt", qualifiedByName = "toISODateTime")
    @Mapping(target = "resource", ignore = true)
    ImageResourceDto imageToImageResourceDto(Image image);

    @Named("toISODateTime")
    default String toISODateTime(LocalDateTime createdAt) {
        return createdAt.format(DateTimeFormatter.ISO_DATE_TIME);
    }


    default ImageDto imageToImageDto(Image image, byte[] data) {
        ImageDto imageDto = imageToImageDto(image);
        imageDto.setBytes(data);
        return imageDto;
    }


    default ImageResourceDto imageToImageResourceDto(Image image, Resource resource) {
        ImageResourceDto imageDto = imageToImageResourceDto(image);
        imageDto.setResource(resource);
        return imageDto;
    }

}
