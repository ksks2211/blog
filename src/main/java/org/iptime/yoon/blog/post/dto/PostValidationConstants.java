package org.iptime.yoon.blog.post.dto;

/**
 * @author rival
 * @since 2024-02-11
 */
public class PostValidationConstants {
    public static final String CATEGORY_DEPTH_REGEX = "^(/\\w+){1,5}$";
    public static final String CATEGORY_DEPTH_MESSAGE = "Category depth cannot be deeper than 5";



    public static final String TAG_RULE_REGEX = "^\\w+( \\w+)*$";
    public static final String TAG_RULE_MESSAGE = "Only single spaces allowed between words";


}
