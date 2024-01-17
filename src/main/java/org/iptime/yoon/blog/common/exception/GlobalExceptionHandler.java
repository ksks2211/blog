package org.iptime.yoon.blog.common.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.iptime.yoon.blog.common.ErrorResponse.createErrorResponse;

/**
 * @author rival
 * @since 2023-09-13
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Entity Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e){
        log.warn(e.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException e){
        log.info(e.getMessage());
        return createErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }



    // Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception e){

        e.printStackTrace();

        log.error(e.getMessage());
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred." );
    }
}
