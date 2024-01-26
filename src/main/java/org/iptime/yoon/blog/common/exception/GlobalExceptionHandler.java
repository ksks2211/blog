package org.iptime.yoon.blog.common.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

import static org.iptime.yoon.blog.common.dto.ErrorResponse.createErrorResponse;

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

        log.warn("Entity not found",e);
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return createErrorResponse(HttpStatus.BAD_REQUEST, message);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e){
        String message = e.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.joining(", "));
        return createErrorResponse(HttpStatus.BAD_REQUEST, message);
    }




    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException e){
        log.error("Access Denied", e);
        return createErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }



    // Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception e){
        log.error("An unexpected error occurred", e);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred." );
    }
}
