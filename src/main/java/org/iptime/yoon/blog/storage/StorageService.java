package org.iptime.yoon.blog.storage;

import org.springframework.core.io.Resource;

/**
 * @author rival
 * @since 2023-08-13
 */
public interface StorageService {
    byte[] download(String objectName) throws Exception;
    void upload(String objectName, String contentType, byte[] data) throws Exception;
    void delete(String objectName) throws Exception;


    Resource downloadAsStream(String filename) throws Exception;
    String getUrl(String objectName);
}
