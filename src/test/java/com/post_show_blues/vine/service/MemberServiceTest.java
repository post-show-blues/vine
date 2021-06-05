package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.dto.Auth.SignupDto;
import com.post_show_blues.vine.dto.member.MemberUpdateDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired AuthService authService;

    @Test
    public void 회원정보수정() throws Exception {
        //given
        SignupDto memberEntityA = createSignupDto();
        Member memberA = authService.join(memberEntityA.toEntity());
        MemberUpdateDto memberUpdateDto = createMemberUpdateDto();


        //when
        Member updateMember = memberService.memberUpdate(memberA.getId(), memberUpdateDto.toEntity());

        //then
        Assertions.assertThat(updateMember.getInstaurl()).isEqualTo(memberUpdateDto.getInstaurl());


    }

    SignupDto createSignupDto(){
        return SignupDto.builder()
                .name("memberB")
                .email("member@duksung.ac.kr")
                .nickname("memberNickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("덕성대학교")
                .build();
    }

    MemberUpdateDto createMemberUpdateDto(){
        return MemberUpdateDto.builder()
                .text("안녕하세요")
                .instaurl("https://www.instagram.com/dlwlrma/?hl=ko")
                .twitterurl("https://twitter.com/BTS_twt?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor")
                .build();
        }

}