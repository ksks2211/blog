package org.iptime.yoon.blog.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rival
 * @since 2023-12-31
 */
@NoArgsConstructor
@Data
public class UnauthorizedAccessResponse {
    private final String message = "Unauthorized Request!";
}
