package org.iptime.yoon.blog.exception;

import jakarta.persistence.EntityNotFoundException;

/**
 * @author rival
 * @since 2023-09-20
 */
public class CategoryEntityNotFoundException extends EntityNotFoundException {
    public CategoryEntityNotFoundException(String fullName) {
        super("Category "+fullName+" is Not Found");
    }
}
