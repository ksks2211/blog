package org.iptime.yoon.blog.service;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.dto.ImageFileDto;
import org.iptime.yoon.blog.dto.res.ImageMetaResDto;
import org.iptime.yoon.blog.entity.ImageMetadata;
import org.iptime.yoon.blog.exception.ImageEntityNotFoundException;
import org.iptime.yoon.blog.repository.ImageMetadataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
/**
 * @author rival
 * @since 2023-08-13
 */

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    private final StorageService storageService;
    private final ImageMetadataRepository imageMetadataRepository;

    @Transactional
    public ImageMetaResDto uploadImageFile(MultipartFile multipartFile,String filename) throws Exception {
        storageService.uploadFile(filename, multipartFile);
        ImageMetadata imageMetadata = ImageMetadata.builder()
            .filename(filename)
            .originalName(multipartFile.getOriginalFilename())
            .contentType(multipartFile.getContentType())
            .size(multipartFile.getSize())
            .build();
        imageMetadataRepository.save(imageMetadata);
        return ImageMetaResDto.createImageMetaDto(imageMetadata);
    }


    private ImageMetadata getImageMetadata(Long id){
        return imageMetadataRepository.findById(id).orElseThrow(() -> new ImageEntityNotFoundException(id));
    }

    private byte[] getImageBytes(String filename) throws Exception {
        return storageService.downloadFile(filename);
    }
    @Transactional
    public ImageFileDto downloadImageFile(Long id) throws Exception {
        ImageMetadata imageMetadata = getImageMetadata(id);
        byte[] bytes = getImageBytes(imageMetadata.getFilename());
        return ImageFileDto.createImageFile(imageMetadata, bytes);
    }


    @Transactional
    public ImageFileDto downloadImageThumbnailFile(Long id) throws Exception {
        ImageMetadata imageMetadata = getImageMetadata(id);
        byte[] bytes = getImageBytes(imageMetadata.getFilename()+".thumb");
        return ImageFileDto.createImageFile(imageMetadata, bytes);
    }

    @Transactional
    public void deleteImageFile(Long id) {
        ImageMetadata imageMetadata = getImageMetadata(id);
        // storageService.deleteFile(imageMetadata.getFilename());
        imageMetadata.softDelete();
        imageMetadataRepository.save(imageMetadata);
    }

}
