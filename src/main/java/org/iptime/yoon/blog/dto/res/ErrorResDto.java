package org.iptime.yoon.blog.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author rival
 * @since 2023-08-12
 */

@Data
@AllArgsConstructor
@Builder
public class ErrorResDto {
    private int statusCode;
    private String message;
    private Exception exception;


    public static ResponseEntity<?> createErrorResponse(HttpStatus httpStatus, String message){
        return ResponseEntity
            .status(httpStatus)
            .body(ErrorResDto.builder()
                .message(message)
                .statusCode(httpStatus.value()).build());
    }
}
