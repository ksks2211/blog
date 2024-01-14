package org.iptime.yoon.blog.common.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.common.ErrorResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rival
 * @since 2024-01-15
 */
@RestController
@Slf4j
public class GlobalErrorController implements ErrorController {
    
    @RequestMapping("/error")
    public ResponseEntity<?> handleGlobalError(HttpServletRequest request){
        Object status  = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            // Handle different status codes or log the error
            Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
            String errorMessage = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
            String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
            ErrorResponse body = ErrorResponse.builder().statusCode(statusCode).message(errorMessage).build();

            log.error("Global Error : ",exception);
            log.error("Request URI : {}",requestUri);
            return ResponseEntity.status(statusCode).body(body);
        }

        ErrorResponse body = ErrorResponse.builder().statusCode(500).message("Unknown Error").build();
        log.error("Global Error :  Unknown");
        return  ResponseEntity.internalServerError().body(body);
    }
}
