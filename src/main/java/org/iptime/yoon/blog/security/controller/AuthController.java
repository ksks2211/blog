package org.iptime.yoon.blog.security.controller;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.security.dto.req.BlogUserRegisterReqDto;
import org.iptime.yoon.blog.security.dto.res.BlogUserRegisterResDto;
import org.iptime.yoon.blog.security.service.BlogUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author rival
 * @since 2023-08-14
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final BlogUserService blogUserService;


    // UsernameAlreadyTakenException
    @PostMapping("/register")
    public ResponseEntity<?> createBlogUser(@RequestBody BlogUserRegisterReqDto registerReqDto){
        blogUserService.createBlogUser(registerReqDto);
        BlogUserRegisterResDto body = new BlogUserRegisterResDto("Registered");
        return new ResponseEntity<>(body,HttpStatus.CREATED);
    }


    // "/log-in" : JwtLoginFilter
    // "/refresh" : JwtRefreshFilter


    // "/log-out"



    // "/is-username-taken?username=xyzxyz"
    @GetMapping("/is-username-taken")
    public ResponseEntity<String> isUsernameTaken(@RequestParam String username) {
        boolean taken = blogUserService.isUsernameTaken(username);
        if (taken) {
            return new ResponseEntity<>("Username is already taken", HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>("Username is available", HttpStatus.OK);
        }
    }

}
