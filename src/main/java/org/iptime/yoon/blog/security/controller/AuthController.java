package org.iptime.yoon.blog.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.annotation.CurrentUsername;
import org.iptime.yoon.blog.security.dto.req.BlogUserRegisterReqDto;
import org.iptime.yoon.blog.security.dto.req.BlogUserUpdateReqDto;
import org.iptime.yoon.blog.security.dto.res.BlogUserInfoResDto;
import org.iptime.yoon.blog.security.exception.UsernameAlreadyTakenException;
import org.iptime.yoon.blog.security.service.BlogUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.iptime.yoon.blog.dto.res.ErrorResDto.createErrorResponse;

/**
 * @author rival
 * @since 2023-08-14
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final BlogUserService blogUserService;


    // UsernameAlreadyTakenException
    @PostMapping({"/register","/sign-up"})
    public ResponseEntity<?> createBlogUser(@RequestBody BlogUserRegisterReqDto registerReqDto){
        BlogUserInfoResDto body = blogUserService.createBlogUser(registerReqDto);
        return new ResponseEntity<>(body,HttpStatus.CREATED);
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
    public ResponseEntity<String> isUsernameTaken(@RequestParam String username) {
        boolean taken = blogUserService.isUsernameTaken(username);
        if (taken) {
            return new ResponseEntity<>("Username is already taken", HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>("Username is available", HttpStatus.OK);
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
    public ResponseEntity<?> updateBlogUserInformation(@CurrentUsername String username, @RequestBody BlogUserUpdateReqDto dto){
        BlogUserInfoResDto blogUserInfo = blogUserService.updateBlogUser(username, dto);
        return ResponseEntity.ok(blogUserInfo);
    }


    // GET "/who-am-i"
    @GetMapping("/who-am-i")
    public ResponseEntity<?> whoAmI(@CurrentUsername String username){
        BlogUserInfoResDto blogUserInfo = blogUserService.getBlogUserInfo(username);
        return ResponseEntity.ok(blogUserInfo); // 200
    }

}
