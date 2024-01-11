package org.iptime.yoon.blog.post;

import jakarta.persistence.EntityNotFoundException;

/**
 * @author rival
 * @since 2023-08-12
 */
public class PostEntityNotFoundException extends EntityNotFoundException {
    public PostEntityNotFoundException(Long id) {
        super("The request post with ID "+id+" was not found");
    }
}
