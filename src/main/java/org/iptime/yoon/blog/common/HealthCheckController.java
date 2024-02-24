package org.iptime.yoon.blog.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.iptime.yoon.blog.common.dto.ErrorResponse;
import org.iptime.yoon.blog.common.dto.MsgResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

/**
 * @author rival
 * @since 2023-08-30
 */

@RestController
@RequestMapping(value="/api/health", produces = {MediaType.APPLICATION_JSON_VALUE})
public class HealthCheckController {




    @GetMapping(value = "" )
    @Operation(summary = "Check Server", description = "Check if server is active and working!")
    public ResponseEntity<?> checkHealth(){
        var body = new MsgResponse("Server is alive");
        return ResponseEntity.ok(body);
    }

    private ResponseEntity<?> fallbackMethod(Throwable t) {
        var body = new MsgResponse("Server is down");
        return ResponseEntity.ok(body);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Check if the client is ADMIN",
        description = "Return OK response only when the client have ADMIN role.",
        responses = {
            @ApiResponse(
              responseCode = "200",
              description = "Client is ADMIN",
              content = @Content(schema = @Schema(implementation = MsgResponse.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Client is not ADMIN",
                content=@Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<?> admin(){
        var body = new MsgResponse("You are an admin");
        return ResponseEntity.ok(body);
    }
    //ADMIN, MANAGER, USER


    @Operation(
        summary = "Check if the client is MANAGER",
        description = "Return OK response only when the client have MANAGER role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Client is MANAGER",
                content = @Content(schema = @Schema(implementation = MsgResponse.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Client is not MANAGER",
                content=@Content(schema = @Schema(implementation = MsgResponse.class)))
        }
    )
    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> manager(){
        var body = new MsgResponse("You are a manager");
        return ResponseEntity.ok(body);
    }


    @Operation(
        summary = "Check if the client is USER",
        description = "Return OK response only when the client have USER role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Client is USER",
                content = @Content(schema = @Schema(implementation = MsgResponse.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Client is not USER",
                content=@Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> user(){
        var body = new MsgResponse("You are a user");
        return ResponseEntity.ok(body);
    }
}
