package org.iptime.yoon.blog.exception;

/**
 * @author rival
 * @since 2023-10-01
 */
public class CategoryNotEmptyException extends Exception{
    public CategoryNotEmptyException(String fullName) {
        super("Category : "+fullName+ " is not empty");
    }
}
