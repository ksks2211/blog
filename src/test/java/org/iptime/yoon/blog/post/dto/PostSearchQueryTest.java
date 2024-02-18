package org.iptime.yoon.blog.post.dto;

import org.iptime.yoon.blog.helper.ValidationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rival
 * @since 2024-02-18
 */
class PostSearchQueryTest extends ValidationTest {


    @Test
    public void testValidation(){

        String writer= "writer";
        String tag = "correct tag";
        String tag2 = "incorrect tag -/92432\t";
        PostSearchQuery searchQuery = new PostSearchQuery();

        searchQuery.setWriter(writer);
        searchQuery.getTags().add(tag);
        var violations = validator.validate(searchQuery);
        assertTrue(violations.isEmpty());

        searchQuery.getTags().add(tag2);
        violations = validator.validate(searchQuery);
        assertFalse(violations.isEmpty());
        assertEquals(1,violations.size());

    }
}