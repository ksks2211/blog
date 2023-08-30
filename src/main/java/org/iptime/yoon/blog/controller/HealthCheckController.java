package org.iptime.yoon.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rival
 * @since 2023-08-30
 */

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping("")
    public ResponseEntity<String> checkHealth(){
        return ResponseEntity.ok("Server is alive");
    }
}
