package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.dto.memberImg.MemberImgUploadDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Log4j2
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

        MemberImgUploadDto memberImgEntityA= memberImgUploadDto();

        //when
        authService.join(memberEntityA.toEntity(), memberImgEntityA);

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> authService.isDuplicateNickname(nickname));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

    }

    @Test
    public void 회원가입() throws Exception {
        //given
        SignupDto memberEntityA = createSignupDto();

        MemberImgUploadDto memberImgEntityA= memberImgUploadDto();

        //when
        Object[] join = authService.join(memberEntityA.toEntity(), memberImgEntityA);
        Member memberA = (Member) join[0];
        MemberImg memberAImg = (MemberImg) join[1];

        //then
        assertThat(memberA.getNickname()).isEqualTo(memberEntityA.getNickname());
        log.info("===========");
        assertThat(memberAImg.getFileName().split("_")[1]).isEqualTo(memberImgEntityA.getFile().getOriginalFilename());

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

    MemberImgUploadDto memberImgUploadDto() throws IOException {
        MockMultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        return MemberImgUploadDto.builder()
                .file(file1)
                .build();
    }



}