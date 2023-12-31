package org.iptime.yoon.blog.image.exception;

import jakarta.persistence.EntityNotFoundException;

/**
 * @author rival
 * @since 2023-08-13
 */
public class ImageEntityNotFoundException extends EntityNotFoundException {

    public ImageEntityNotFoundException(Long id){
        super("The request image with ID "+id+" was not found");
    }
}
