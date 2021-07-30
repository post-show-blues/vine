package com.post_show_blues.vine.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Log4j2
@Controller
public class AuthController {

    @GetMapping("/auth/signin") //로그인 창 출력
    public String singinForm(){
        return "signin";
    }

    @GetMapping("/auth/signup") //회원가입 창 출력
    public String singupForm(){
        return "";
    }
}
