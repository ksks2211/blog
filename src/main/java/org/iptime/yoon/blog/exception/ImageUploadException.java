package org.iptime.yoon.blog.exception;

/**
 * @author rival
 * @since 2023-08-13
 */
public class ImageUploadException extends ImageException{

    public ImageUploadException(String filename, Exception cause) {
        super("Error uploading file: " + filename, cause);
    }

}
