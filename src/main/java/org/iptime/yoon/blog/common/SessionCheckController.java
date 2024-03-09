package org.iptime.yoon.blog.common;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rival
 * @since 2024-03-09
 */

@RestController
@RequestMapping("/api/session")
public class SessionCheckController {

    @GetMapping("/create")
    public String getSessionId(HttpSession httpSession){
        httpSession.setAttribute("hello","world");
        return "Created!";
    }

    @GetMapping("/read")
    public String readSession(HttpSession httpSession){
        String read =  (String) httpSession.getAttribute("hello");

        if(read==null){
            read="non";
        }

        return read;
    }


    @GetMapping("/delete")
    public String deleteSession(HttpSession httpSession){
        String sessionId = httpSession.getId();

        httpSession.invalidate();
        return "Delete Session : "+sessionId;
    }

}
