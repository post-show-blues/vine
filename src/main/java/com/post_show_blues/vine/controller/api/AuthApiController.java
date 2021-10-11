package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.auth.SigninDto;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.dto.auth.SignupResponse;
import com.post_show_blues.vine.dto.auth.TokenDto;
import com.post_show_blues.vine.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestPart String nickname,
                                    @RequestPart String email,
                                    @RequestPart String password,
                                    @RequestPart(required = false) MultipartFile file) throws IOException {
        SignupDto signupDto = SignupDto.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .file(file)
                .build();
        log.info(signupDto);

        SignupResponse join = authService.join(signupDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "회원가입 성공", join), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SigninDto signinDto) {
        String token = authService.login(signinDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);

        return new ResponseEntity<>(new CMRespDto<>(1, "로그인 성공", new TokenDto(token)), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/check/nickname")
    public ResponseEntity<CMRespDto> isDuplicateNickname(@RequestParam String nickname) {
        authService.isDuplicateNickname(nickname);

        return new ResponseEntity<>(new CMRespDto<>(1, "중복되지 않은 닉네임입니다", null), HttpStatus.OK);
    }

    @GetMapping("/check/email")
    public ResponseEntity<CMRespDto> isDuplicateEmail(@RequestParam String email) {
        authService.isDuplicateEmail(email);

        return new ResponseEntity<>(new CMRespDto<>(1, "중복되지 않은 이메일입니다", null), HttpStatus.OK);
    }
}
