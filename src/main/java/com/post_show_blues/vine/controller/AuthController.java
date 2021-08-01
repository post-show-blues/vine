package com.post_show_blues.vine.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/auth")
@Controller
public class AuthController {

    @GetMapping("/signin") //로그인 창 출력
    public String singinForm(){
        return "signin";
    }

    @GetMapping("/signup") //회원가입 창 출력
    public String singupForm(){
        return "";
    }
}
