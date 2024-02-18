package org.iptime.yoon.blog.category;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author rival
 * @since 2024-02-18
 */
class CategoryUtilsTest {

    @Test
    public void util_test(){

        String categoryString = "username-dir1-dir2";
        String parsed = CategoryUtils.parseCategoryString(categoryString);

        assertEquals("/dir1/dir2",parsed);
    }

}