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
    private String error;
    private String message;
}
