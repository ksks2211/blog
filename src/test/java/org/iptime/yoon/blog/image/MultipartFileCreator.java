package org.iptime.yoon.blog.image;

/**
 * @author rival
 * @since 2024-02-20
 */
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileCreator {
    public static MultipartFile createMultipartFile(byte[] imageBytes, String name, String originalFileName, String contentType) {
        return new MockMultipartFile(name, originalFileName, contentType, imageBytes);
    }
}
