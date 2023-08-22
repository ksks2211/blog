package org.iptime.yoon.blog.service;

/**
 * @author rival
 * @since 2023-08-13
 */
public interface StorageService {
    byte[] download(String filename) throws Exception;
    void upload(String objectName, String contentType, byte[] data) throws Exception;
    void delete(String filename) throws Exception;
}
