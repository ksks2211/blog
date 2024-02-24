package org.iptime.yoon.blog.image;

import org.iptime.yoon.blog.image.exception.ImageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rival
 * @since 2024-02-20
 */
class ImageMapperTest {

    private ImageMapper imageMapper;

    @BeforeEach
    public void init() {
        imageMapper = Mappers.getMapper(ImageMapper.class);
    }


    @Test
    public void testImageMapper() throws IOException {

        MultipartFile multipartFile = ImageTestHelper.generateTestImageFile(250, 400);

        Image image = Image.builder()
            .id(1L)
            .contentType(multipartFile.getContentType())
            .originalName(multipartFile.getOriginalFilename())
            .size(multipartFile.getSize())
            .filename("filename.png")
            .build();
        image.setCreatedAt(LocalDateTime.now());

        ImageDto imageDto = imageMapper.imageToImageDto(image, multipartFile.getBytes());

        assertEquals(image.getContentType(), imageDto.getContentType());
        assertEquals(image.getOriginalName(), imageDto.getOriginalName());
        assertEquals(image.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME),imageDto.getCreatedDate());
        assertEquals(image.getId(),imageDto.getId());
    }

}