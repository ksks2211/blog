package org.iptime.yoon.blog.exception;

/**
 * @author rival
 * @since 2023-08-13
 */
public class ImageDownloadException extends Exception{

    public ImageDownloadException(String filename, Exception cause) {
        super("Error downloading file: " + filename, cause);
    }
}
