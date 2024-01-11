package org.iptime.yoon.blog.category;

import java.util.Arrays;

/**
 * @author rival
 * @since 2024-01-02
 */
public class CategoryUtils {
    public static String parseCategoryString(String categoryString) {
        // validate string logic
        String[] parts = categoryString.split("-");
        return "/" + String.join("/", Arrays.copyOfRange(parts, 1, parts.length));
    }
}
