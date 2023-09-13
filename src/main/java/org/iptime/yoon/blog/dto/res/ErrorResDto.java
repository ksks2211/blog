package org.iptime.yoon.blog.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author rival
 * @since 2023-08-12
 */

@Data
@AllArgsConstructor
@Builder
public class ErrorResDto {
    private int status;
    private String message;
    private Exception exception;
}
