package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.dto.Auth.SignupDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AuthServiceTest {
    @Autowired AuthService authService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 중복닉네임() throws Exception {
        //given
        SignupDto memberEntityA = createSignupDto();

        String nickname="memberNickname";

        //when
        Member memberA = authService.join(memberEntityA.toEntity());

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> authService.isDuplicateNickname(nickname));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

    }

    @Test
    public void 회원가입() throws Exception {
        //given
        SignupDto memberEntityA = createSignupDto();


        //when
        Member memberA = authService.join(memberEntityA.toEntity());

        //then
        assertThat(memberA.getNickname()).isEqualTo(memberEntityA.getNickname());

    }

    SignupDto createSignupDto(){
        return SignupDto.builder()
                .name("memberA")
                .email("member@kookmin.ac.kr")
                .nickname("memberNickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();
    }



}