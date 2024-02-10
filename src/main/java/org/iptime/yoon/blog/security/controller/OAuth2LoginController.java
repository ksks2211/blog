package org.iptime.yoon.blog.security.controller;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author rival
 * @since 2024-02-03
 */

@RestController
public class OAuth2LoginController {

    private String oauth2PageContent;


    @PostConstruct
    public void init() throws IOException {
        ClassPathResource cpr = new ClassPathResource("static/oauth2.html");
        oauth2PageContent = new String(Files.readAllBytes(Paths.get(cpr.getURI())));
    }

    @GetMapping(value = "/login/google")
    public String getOauth2LoginHandlingPage(){
        return oauth2PageContent;
    }

    @GetMapping(value = "/login/complete")
    public void clearSession(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);
        if(session!=null){
            session.invalidate();
        }
    }


}
