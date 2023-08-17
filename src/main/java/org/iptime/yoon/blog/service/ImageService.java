package org.iptime.yoon.blog.service;

import org.iptime.yoon.blog.dto.ImageFileDto;
import org.iptime.yoon.blog.dto.res.ImageMetaResDto;
import org.iptime.yoon.blog.entity.ImageMetadata;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author rival
 * @since 2023-08-17
 */
public interface ImageService {
    ImageMetaResDto uploadImageFile(MultipartFile multipartFile, String filename) throws Exception;

    ImageFileDto downloadImageFile(Long id) throws Exception;

    void deleteImageFile(Long id);

    ImageFileDto downloadImageThumbnailFile(Long id) throws Exception;
}
