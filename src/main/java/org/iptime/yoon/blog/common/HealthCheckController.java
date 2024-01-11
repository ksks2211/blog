package org.iptime.yoon.blog.common;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> admin(){
        return ResponseEntity.ok("You are an admin");
    }
    //ADMIN, MANAGER, USER
    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> manager(){
        return ResponseEntity.ok("You are a manager");
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> user(){
        return ResponseEntity.ok("You are a user");
    }
}
