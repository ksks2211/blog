package org.iptime.yoon.blog.exception;

/**
 * @author rival
 * @since 2023-10-01
 */
public class CategoryNotEmptyException extends RuntimeException{
    public CategoryNotEmptyException(String fullName) {
        super("Category : "+fullName+ " is not empty");
    }
}
