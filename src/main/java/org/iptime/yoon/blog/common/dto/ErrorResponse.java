package org.iptime.yoon.blog.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author rival
 * @since 2023-08-12
 */

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private Integer statusCode;
    private String message;
    private String timestamp;

    public String getError(){
        return message;
    }

    public static ErrorResponse getErrorResponseBody(HttpStatus httpStatus, String message){
        return ErrorResponse.builder()
            .message(message)
            .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
            .statusCode(httpStatus.value()).build();
    }

    public static ResponseEntity<?> createErrorResponse(HttpStatus httpStatus, String message){
        return ResponseEntity
            .status(httpStatus)
            .body(getErrorResponseBody(httpStatus, message));
    }
}
