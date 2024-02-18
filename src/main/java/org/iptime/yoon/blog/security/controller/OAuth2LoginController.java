package org.iptime.yoon.blog.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rival
 * @since 2024-02-03
 */

@RestController
public class OAuth2LoginController {

    @GetMapping(value = "/login/complete")
    public void clearSession(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);
        if(session!=null){
            session.invalidate();
        }
    }


}
