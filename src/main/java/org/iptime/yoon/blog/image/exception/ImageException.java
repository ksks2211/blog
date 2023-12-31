package org.iptime.yoon.blog.image.exception;

/**
 * @author rival
 * @since 2023-09-13
 */
public abstract class ImageException extends Exception {

    public ImageException(String message, Exception cause){
        super(message,cause);
    }
}
