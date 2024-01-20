package org.iptime.yoon.blog.common.exception;

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

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        String errorMessage = (String) request.getAttribute("javax.servlet.error.message");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");

        log.error("Servlet Error for URI - {}",requestUri,exception);

        ErrorResponse body = ErrorResponse.builder().statusCode(statusCode).message(errorMessage).build();
        return  ResponseEntity.status(statusCode).body(body);
    }

}
