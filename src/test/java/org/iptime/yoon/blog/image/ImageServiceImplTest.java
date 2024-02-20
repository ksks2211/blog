package org.iptime.yoon.blog.image;

import org.iptime.yoon.blog.image.exception.ImageEntityNotFoundException;
import org.iptime.yoon.blog.storage.StorageService;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @author rival
 * @since 2024-02-20
 */

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {


    @Mock
    private StorageService storageService;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;


    private MultipartFile generateTestImageFile(int width, int height) throws IOException {
        byte[] imageBytes = ImageFileCreator.createSampleImage(width, height);
        return  MultipartFileCreator.createMultipartFile(imageBytes, "testImage", "testImage.png", "image/png");
    }



    @Test
    void testCreateThumbnail() throws Exception {

        MultipartFile multipartFile = generateTestImageFile(200, 200);
        String filename = multipartFile.getOriginalFilename();
        Long userId = 123L;
        Long imageId = 1L;
        Image savedImage = Image.builder()
            .filename(filename)
            .owner(BlogUser.builder().id(userId).build())
            .originalName(multipartFile.getOriginalFilename())
            .contentType(multipartFile.getContentType())
            .size(multipartFile.getSize())
            .id(imageId)
            .build();

        doNothing().when(storageService).upload(any(), any(), any());
        when(imageRepository.save(any(Image.class))).thenReturn(savedImage);
        when(imageRepository.findById(any(Long.class))).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            if(id.equals(imageId)){
                return Optional.of(savedImage);
            }else{
                return Optional.empty();
            }
        });


        Long id = imageService.uploadImage(multipartFile, filename, userId);
        Image foundImage = imageService.getImageById(id);

        assertEquals(imageId, id);
        assertNotNull(foundImage);

        assertThrows(ImageEntityNotFoundException.class, ()->{
           imageService.getImageById(100L);
        });
    }

}