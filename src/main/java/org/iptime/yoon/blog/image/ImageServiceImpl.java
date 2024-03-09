package org.iptime.yoon.blog.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.iptime.yoon.blog.image.exception.ImageEntityNotFoundException;
import org.iptime.yoon.blog.image.exception.ImageMapper;
import org.iptime.yoon.blog.image.exception.ImageUploadException;
import org.iptime.yoon.blog.storage.StorageService;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
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
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private static final int THUMBNAIL_WIDTH = 100;
    private static final int THUMBNAIL_HEIGHT = 100;


    private byte[] createThumbnail(byte[] imageData) throws IOException {
        try (ByteArrayOutputStream thumbnailOutput = new ByteArrayOutputStream()) {
            Thumbnailator.createThumbnail(new ByteArrayInputStream(imageData), thumbnailOutput , THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
            return thumbnailOutput.toByteArray();
        }
    }


    @Transactional
//    @CircuitBreaker(name = "backendA", fallbackMethod = "fallbackResponse")
    public String uploadImage(MultipartFile multipartFile, String filename, Long userId) throws Exception {
        try{
            byte[] imageData = multipartFile.getBytes();


//            byte[] thumbData = createThumbnail(imageData);
            storageService.upload(filename, multipartFile.getContentType(), imageData);
//            storageService.upload(filename + ".thumb", multipartFile.getContentType(), thumbData);
        } catch(Exception e) {
            log.error("Failed to upload file: " + filename, e);
            throw new ImageUploadException(filename, e);
        }


        String imageUrl = storageService.getUrl(filename);

        Image image = Image.builder()
            .filename(filename)
            .owner(BlogUser.builder().id(userId).build())
            .originalName(multipartFile.getOriginalFilename())
            .contentType(multipartFile.getContentType())
            .size(multipartFile.getSize())
            .imageUrl(imageUrl)
            .build();
        return imageRepository.save(image).getImageUrl();
    }


    public Long fallbackResponse(Exception e) {
        log.info("Fallback in image service", e);
        return -1L;
    }


    public Image getImageById(Long imageId){
        return imageRepository.findById(imageId).orElseThrow(() -> new ImageEntityNotFoundException(imageId));
    }



    public byte[] getImageBytes(String filename) throws Exception {
        return storageService.download(filename);
    }
    @Transactional
    public ImageDto downloadImage(Long imageId) throws Exception {
        Image image = getImageById(imageId);
        byte[] bytes = getImageBytes(image.getFilename());
        return imageMapper.imageToImageDto(image, bytes);
    }


    @Transactional
    public ImageResourceDto downloadImageResource(Long imageId) throws Exception {
        Image image = getImageById(imageId);
        Resource resource = storageService.downloadAsStream(image.getFilename());
        return imageMapper.imageToImageResourceDto(image, resource);
    }


    @Transactional
    public ImageDto downloadImageThumbnail(Long imageId) throws Exception {
        Image image = getImageById(imageId);
        byte[] bytes = getImageBytes(image.getFilename()+".thumb");
        return imageMapper.imageToImageDto(image, bytes);
    }

    @Transactional
    @CacheEvict(value="images", key="#imageId")
    public void deleteImage(Long imageId) {
        Image image = getImageById(imageId);
        // storageService.deleteFile(image.getFilename());
        image.softDelete();
        imageRepository.save(image);
    }

    @Override
    @Cacheable(value="images",key="#imageId")
    public String getImageUrl(Long imageId) {
        Image image = getImageById(imageId);
        return image.getImageUrl();
    }


}
