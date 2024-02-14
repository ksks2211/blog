package org.iptime.yoon.blog.post.dto;

import jakarta.validation.ConstraintViolation;
import org.iptime.yoon.blog.helper.ValidationTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rival
 * @since 2024-02-14
 */
class PostCreateRequestTest extends ValidationTest {

    @Test
    public void testValidation(){
        String category = "/dir1/dir2";
        String content = "Content of Post";
        String title = "Title of Post";
        PostCreateRequest req = new PostCreateRequest();

        req.setCategory(category);
        req.setTitle(null);
        req.setContent(content);

        var violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertEquals(1,violations.size());

        req.setTitle(title);

        violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }
}