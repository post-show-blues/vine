package com.post_show_blues.vine.controller;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.dto.memberImg.MemberImgUploadDto;
import com.post_show_blues.vine.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Log4j2
@Controller
public class AuthController {

    private final AuthService authService;
    //url, 파일명 매핑해주기

    @GetMapping("/auth/signin") //로그인 창 출력
    public String singinForm(){
        return "";
    }

    @GetMapping("/auth/signup") //회원가입 창 출력
    public String singupForm(){
        return "";
    }

    @PostMapping("/auth/signup") //데이터 전달
    public String singup(@Valid SignupDto signupDto, MemberImgUploadDto memberImgUploadDto){
        Member member = signupDto.toEntity();
        authService.join(member, memberImgUploadDto);
        return "";

    }
}
