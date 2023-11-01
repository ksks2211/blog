package org.iptime.yoon.blog.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.dto.res.ErrorResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author rival
 * @since 2023-09-13
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {





    // Entity Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResDto> handleEntityNotFoundException(EntityNotFoundException e){
        ErrorResDto error = ErrorResDto.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .message(e.getMessage())
            //.exception(e)
            .build();
        log.warn(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResDto> handleAccessDenied(AccessDeniedException e){
        ErrorResDto error = ErrorResDto.builder()
            .status(HttpServletResponse.SC_FORBIDDEN)
            .message(e.getMessage())
            //.exception(accessDeniedException)
            .build();
        log.info(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);

    }

    // Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResDto> handleGenericException(Exception e){
        ErrorResDto error = ErrorResDto.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("An unexpected error occurred.")
            //.exception(e)
            .build();
        log.error(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
