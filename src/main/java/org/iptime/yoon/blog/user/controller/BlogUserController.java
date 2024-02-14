package org.iptime.yoon.blog.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.common.dto.ErrorResponse;
import org.iptime.yoon.blog.common.dto.MsgResponse;
import org.iptime.yoon.blog.security.CurrentUsername;
import org.iptime.yoon.blog.security.exception.UsernameAlreadyTakenException;
import org.iptime.yoon.blog.user.dto.BlogUserConstants;
import org.iptime.yoon.blog.user.dto.BlogUserInfoResponse;
import org.iptime.yoon.blog.user.dto.BlogUserRegisterRequest;
import org.iptime.yoon.blog.user.dto.BlogUserUpdateRequest;
import org.iptime.yoon.blog.user.service.BlogUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.iptime.yoon.blog.common.dto.ErrorResponse.createErrorResponse;

/**
 * @author rival
 * @since 2023-08-14
 */



@Tag(name = "User Management", description = "APIs for managing user account")
@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Validated
public class BlogUserController {

    private final BlogUserService blogUserService;


    @Operation(
        summary = "Create new account",
        description = "Create new account with username and password",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "User registered",
                content = @Content(schema = @Schema(implementation = BlogUserInfoResponse.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "User fail to sign up",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
        }
    )
    @PostMapping(value = {"/register", "/sign-up"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBlogUser(@Valid @RequestBody BlogUserRegisterRequest requestBody) {
        BlogUserInfoResponse responseBody = blogUserService.createBlogUser(requestBody);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }


    @Operation(
        summary = "Check Username is available",
        description = "Check Username is available for new user",
        responses = {
            @ApiResponse(
                responseCode = "409",
                description = "Username is not available",
                content = @Content(schema = @Schema(implementation = MsgResponse.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Username is invalid",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                responseCode = "200",
                description = "Username is available",
                content = @Content(schema = @Schema(implementation = MsgResponse.class))
            )
        }
    )
    @GetMapping("/is-username-taken")
    public ResponseEntity<?> isUsernameTaken(
        @RequestParam
        @NotBlank(message = "Username is mandatory")
        @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
        @Pattern(regexp = BlogUserConstants.USERNAME_PATTERN_REGEX, message = BlogUserConstants.USERNAME_PATTERN_MESSAGE)
        String username) {
        boolean taken = blogUserService.isUsernameTaken(username.trim());
        MsgResponse body = new MsgResponse();
        if (taken) {
            body.setMessage("Username is already taken");
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);
        }

        body.setMessage("Username is available");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    // DELETE "/unregister"
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping({"/unregister"})
    public ResponseEntity<?> unregisterBlogUser(@CurrentUsername String username) {
        blogUserService.deleteBlogUser(username);
        return ResponseEntity.noContent().build(); // 204
    }


    // PUT "/update"
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update")
    public ResponseEntity<?> updateBlogUserInformation(@CurrentUsername String username, @Valid @RequestBody BlogUserUpdateRequest requestBody) {
        BlogUserInfoResponse responseBody = blogUserService.updateBlogUser(username, requestBody);
        return ResponseEntity.ok(responseBody);
    }


    // GET "/who-am-i" + Authenticated User

    @GetMapping("/who-am-i")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> whoAmI(@CurrentUsername String username) {
        BlogUserInfoResponse responseBody = blogUserService.getBlogUserInfo(username);
        return ResponseEntity.ok(responseBody); // 200
    }


    @GetMapping("/check-auth")
    public Map<String, ?> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
            && authentication.isAuthenticated()
            && !(authentication instanceof AnonymousAuthenticationToken);

        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", isAuthenticated);
        response.put("principal", authentication);
        return response;

    }


    @ExceptionHandler(value = UsernameAlreadyTakenException.class)
    public ResponseEntity<?> usernameAlreadyTakenExceptionHandler(UsernameAlreadyTakenException e) {
        log.info(e.getClass().getName());
        log.info(e.getMessage());
        return createErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

}
