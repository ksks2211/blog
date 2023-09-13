package org.iptime.yoon.blog.exception;

import jakarta.persistence.EntityNotFoundException;
import org.iptime.yoon.blog.dto.res.ErrorResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author rival
 * @since 2023-09-13
 */

@ControllerAdvice
public class GlobalExceptionHandler {





    // Entity Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResDto> handleEntityNotFoundException(EntityNotFoundException e){
        ErrorResDto error = ErrorResDto.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .message(e.getMessage())
            .exception(e)
            .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    // Authentication Failed
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResDto> handleAuthenticationException(AuthenticationException e){
        ErrorResDto error = ErrorResDto.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .message(e.getMessage())
            .exception(e).build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }


    // Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResDto> handleGenericException(Exception e){
        ErrorResDto error = ErrorResDto.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("An unexpected error occurred.")
            .exception(e).build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
