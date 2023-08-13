package org.iptime.yoon.blog.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author rival
 * @since 2023-08-13
 */
public interface StorageService {
    void uploadFile(String filename, MultipartFile multipartFile) throws Exception;
    byte[] downloadFile(String filename) throws Exception;
    void deleteFile(String filename) throws Exception;
}
