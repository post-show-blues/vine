package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/auth/signup") //데이터 전달
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupDto){
        System.out.println(signupDto);
        Object[] join = authService.join(signupDto);
        return new ResponseEntity<>(new CMRespDto<>(1, "회원가입 성공", join), HttpStatus.OK);
    }

}
