package com.example.redis.practice_4_my;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {


    @GetMapping("/login")
    public String login() {
        return "login-form";
    }

    @GetMapping("/my-profile")
    public String myProfile() {
        return "my-profile";
    }


}
