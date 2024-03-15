package org.iptime.yoon.blog.category;

import java.util.Arrays;

/**
 * @author rival
 * @since 2024-01-02
 */
public class CategoryUtils {

    // e.g. user123-java-spring => /java/spring
    public static String parseCategoryString(String categoryString) {
        String[] parts = categoryString.split("-");
        return "/" + String.join("/", Arrays.copyOfRange(parts, 1, parts.length));
    }
}
