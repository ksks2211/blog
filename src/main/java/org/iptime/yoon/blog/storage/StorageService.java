package org.iptime.yoon.blog.storage;

/**
 * @author rival
 * @since 2023-08-13
 */
public interface StorageService {
    byte[] download(String objectName) throws Exception;
    void upload(String objectName, String contentType, byte[] data) throws Exception;
    void delete(String objectName) throws Exception;

    String getUrl(String objectName);
}
