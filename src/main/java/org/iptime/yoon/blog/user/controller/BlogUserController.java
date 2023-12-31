package org.iptime.yoon.blog.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.annotation.CurrentUsername;
import org.iptime.yoon.blog.user.dto.BlogUserRegisterRequest;
import org.iptime.yoon.blog.user.dto.BlogUserUpdateRequest;
import org.iptime.yoon.blog.user.dto.BlogUserInfoResponse;
import org.iptime.yoon.blog.security.exception.UsernameAlreadyTakenException;
import org.iptime.yoon.blog.user.service.BlogUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.iptime.yoon.blog.dto.res.ErrorResDto.createErrorResponse;

/**
 * @author rival
 * @since 2023-08-14
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class BlogUserController {

    private final BlogUserService blogUserService;

    // UsernameAlreadyTakenException
    @PostMapping({"/register","/sign-up"})
    public ResponseEntity<?> createBlogUser(@RequestBody BlogUserRegisterRequest requestBody){
        BlogUserInfoResponse responseBody = blogUserService.createBlogUser(requestBody);
        return new ResponseEntity<>(responseBody,HttpStatus.CREATED);
    }

    @ExceptionHandler(value= UsernameAlreadyTakenException.class)
    public ResponseEntity<?> usernameAlreadyTakenExceptionHandler(UsernameAlreadyTakenException e){
        log.info(e.getClass().getName());
        log.info(e.getMessage());
        return createErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }


    // "/log-in" : JwtLoginFilter
    // "/refresh" : JwtRefreshFilter
    // "/log-out"



    // GET "/is-username-taken?username=xyzxyz"
    @GetMapping("/is-username-taken")
    public ResponseEntity<?> isUsernameTaken(@RequestParam String username) {
        boolean taken = blogUserService.isUsernameTaken(username);
        Map<String,String> body = new HashMap<>();
        body.put("message","Username is available");
        if (taken) {
            body.put("message", "Username is already taken");
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
    }

    // DELETE "/unregister"
    @DeleteMapping({"/unregister"})
    public ResponseEntity<?> unregisterBlogUser(@CurrentUsername String username){
        blogUserService.deleteBlogUser(username);
        return ResponseEntity.noContent().build(); // 204
    }


    // PUT "/update"
    @PutMapping("/update")
    public ResponseEntity<?> updateBlogUserInformation(@CurrentUsername String username, @RequestBody BlogUserUpdateRequest requestBody){
        BlogUserInfoResponse responseBody = blogUserService.updateBlogUser(username, requestBody);
        return ResponseEntity.ok(responseBody);
    }


    // GET "/who-am-i" + Authenticated User

    @GetMapping("/who-am-i")
    public ResponseEntity<?> whoAmI(@CurrentUsername String username){
        BlogUserInfoResponse responseBody = blogUserService.getBlogUserInfo(username);
        return ResponseEntity.ok(responseBody); // 200
    }

}
