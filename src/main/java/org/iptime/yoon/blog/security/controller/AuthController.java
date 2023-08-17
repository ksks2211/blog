package org.iptime.yoon.blog.security.controller;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.security.dto.res.BlogUserRegisterResDto;
import org.iptime.yoon.blog.security.dto.req.BlogUserRegisterReqDto;
import org.iptime.yoon.blog.security.service.BlogUserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rival
 * @since 2023-08-14
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final BlogUserServiceImpl blogUserService;


    // UsernameAlreadyTakenException
    @PostMapping("/register")
    public ResponseEntity<?> createBlogUser(@RequestBody BlogUserRegisterReqDto registerReqDto){
        blogUserService.createBlogUser(registerReqDto);
        BlogUserRegisterResDto body = new BlogUserRegisterResDto("Registered");
        return new ResponseEntity<>(body,HttpStatus.CREATED);
    }


    // "/log-in"
    // "/log-out"
    // "/refresh"





}
