package org.iptime.yoon.blog.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author rival
 * @since 2023-08-12
 */

@Data
@AllArgsConstructor
public class ErrorResDto {

    private String error;
    private String message;
}
