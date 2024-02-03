package org.iptime.yoon.blog.common.dto;

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
public class ErrorResponse {
    private int statusCode;
    private String message;

    public String getError(){
        return message;
    }

    public static ResponseEntity<?> createErrorResponse(HttpStatus httpStatus, String message){
        return ResponseEntity
            .status(httpStatus)
            .body(ErrorResponse.builder()
                .message(message)
                .statusCode(httpStatus.value()).build());
    }
}