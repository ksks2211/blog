package org.iptime.yoon.blog.image.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author rival
 * @since 2023-08-13
 */

@Slf4j
public class ImageDownloadException extends ImageException{

    public ImageDownloadException(String filename, Exception cause) {
        super("Error downloading file: " + filename, cause);
        log.warn(this.getMessage());
    }
}
