package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.auth.SigninDto;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.dto.auth.TokenDto;
import com.post_show_blues.vine.jwt.TokenProvider;
import com.post_show_blues.vine.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupDto) throws IOException {
        System.out.println(signupDto);
        Object[] join = authService.join(signupDto);
        return new ResponseEntity<>(new CMRespDto<>(1, "회원가입 성공", join), HttpStatus.OK);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SigninDto signinDto) {
        String token = authService.login(signinDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);

        return new ResponseEntity<>(new CMRespDto<>(1, "로그인 성공", new TokenDto(token)), httpHeaders, HttpStatus.OK);
    }


}
