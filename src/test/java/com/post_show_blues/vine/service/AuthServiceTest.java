package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.dto.auth.SignupResponse;
import com.post_show_blues.vine.service.auth.AuthService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Log4j2
@Transactional
@SpringBootTest
public class AuthServiceTest {
    @Autowired
    AuthService authService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 중복닉네임() throws Exception {
        //given
        SignupDto memberA = createSignupDto();
        String nickname="memberNickname";

        //when
        SignupResponse join = authService.join(memberA);
        System.out.println(join);

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> authService.join(memberA));

        assertThat(e.getMessage()).isEqualTo("중복된 닉네임입니다");

    }

    @Test
    public void 회원가입_사진o() throws Exception {
        //given
        SignupDto signupDtoImg = createSignupDtoImg();

        //when
        SignupResponse join = authService.join(signupDtoImg);

        //then
        assertThat(join.getNickname()).isEqualTo(signupDtoImg.getNickname());

        assertThat(join.getStoreFileName().split("_")[1]).isEqualTo(signupDtoImg.getFile().getOriginalFilename());

    }

    @Test
    public void 회원가입_사진x() throws Exception {
        //given
        SignupDto memberA = createSignupDto();

        //when
        SignupResponse join = authService.join(memberA);

        //then
        assertThat(memberA.getNickname()).isEqualTo(join.getNickname());
        assertThat(join.getForderPath()).isEqualTo(null);
    }


    SignupDto createSignupDto(){
        return SignupDto.builder()
                .email("member@kookmin.ac.kr")
                .nickname("memberNickname")
                .password("1111")
                .build();
    }

    SignupDto createSignupDtoImg(){
        MockMultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        SignupDto signupDto = createSignupDto();
        signupDto.setFile(file1);
        return signupDto;
    }

}