package org.iptime.yoon.blog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.iptime.yoon.blog.dto.ImageFileDto;
import org.iptime.yoon.blog.dto.res.ImageMetaResDto;
import org.iptime.yoon.blog.entity.ImageMetadata;
import org.iptime.yoon.blog.exception.ImageEntityNotFoundException;
import org.iptime.yoon.blog.exception.ImageUploadException;
import org.iptime.yoon.blog.repository.ImageMetadataRepository;
import org.iptime.yoon.blog.security.entity.BlogUser;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author rival
 * @since 2023-08-13
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    private final StorageService storageService;
    private final ImageMetadataRepository imageMetadataRepository;
    private static final int THUMBNAIL_WIDTH = 100;
    private static final int THUMBNAIL_HEIGHT = 100;


    private byte[] createThumbnail(byte[] imageData) throws IOException {
        try (ByteArrayOutputStream thumbnailOutput = new ByteArrayOutputStream()) {
            Thumbnailator.createThumbnail(new ByteArrayInputStream(imageData), thumbnailOutput , THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
            return thumbnailOutput.toByteArray();
        }
    }


    @Transactional
    public ImageMetaResDto uploadImageFile(MultipartFile multipartFile, String filename, Long userId) throws Exception {
        try{
            byte[] imageData = multipartFile.getBytes();
            byte[] thumbData = createThumbnail(imageData);
            storageService.upload(filename, multipartFile.getContentType(), imageData);
            storageService.upload(filename + ".thumb", multipartFile.getContentType(), thumbData);
        } catch(Exception e) {
            log.error("Failed to upload file: " + filename, e);
            throw new ImageUploadException(filename, e);
        }

        ImageMetadata imageMetadata = ImageMetadata.builder()
            .filename(filename)
            .owner(BlogUser.builder().id(userId).build())
            .originalName(multipartFile.getOriginalFilename())
            .contentType(multipartFile.getContentType())
            .size(multipartFile.getSize())
            .build();
        imageMetadataRepository.save(imageMetadata);
        return ImageMetaResDto.createImageMetaDto(imageMetadata);
    }



    public ImageMetadata getImageMetadata(Long id){
        return imageMetadataRepository.findById(id).orElseThrow(() -> new ImageEntityNotFoundException(id));
    }



    public byte[] getImageBytes(String filename) throws Exception {
        return storageService.download(filename);
    }
    @Transactional
    @Cacheable(value="images", key="#id")
    public ImageFileDto downloadImageFile(Long id) throws Exception {
        ImageMetadata imageMetadata = getImageMetadata(id);
        byte[] bytes = getImageBytes(imageMetadata.getFilename());
        return ImageFileDto.createImageFile(imageMetadata, bytes);
    }


    @Transactional
    @Cacheable(value="thumbnails", key="#id")
    public ImageFileDto downloadImageThumbnailFile(Long id) throws Exception {
        ImageMetadata imageMetadata = getImageMetadata(id);
        byte[] bytes = getImageBytes(imageMetadata.getFilename()+".thumb");
        return ImageFileDto.createImageFile(imageMetadata, bytes);
    }

    @Transactional
    @CacheEvict(value={"images", "thumbnails"}, key="#id")
    public void deleteImageFile(Long id) {
        ImageMetadata imageMetadata = getImageMetadata(id);
        // storageService.deleteFile(imageMetadata.getFilename());
        imageMetadata.softDelete();
        imageMetadataRepository.save(imageMetadata);
    }

}
