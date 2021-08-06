package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.dto.auth.SignupDto;
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
    @Autowired AuthService authService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 중복닉네임() throws Exception {
        //given
        SignupDto memberA = createSignupDto();
        String nickname="memberNickname";

        //when
        authService.join(memberA);

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> authService.isDuplicateNickname(nickname));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

    }

    @Test
    public void 회원가입_사진o() throws Exception {
        //given
        SignupDto signupDtoImg = createSignupDtoImg();

        //when
        Object[] join = authService.join(signupDtoImg);
        Member memberA = (Member) join[0];
        MemberImg memberAImg = (MemberImg) join[1];

        //then
        assertThat(memberA.getNickname()).isEqualTo(signupDtoImg.getNickname());

        assertThat(memberAImg.getStoreFileName().split("_")[1]).isEqualTo(signupDtoImg.getFile().getOriginalFilename());

    }

    @Test
    public void 회원가입_사진x() throws Exception {
        //given
        SignupDto memberA = createSignupDto();

        //when
        Object[] join = authService.join(memberA);
        Member memberEntityA = (Member) join[0];
        Optional<MemberImg> memberAImg = Optional.ofNullable((MemberImg) join[1]);

        //then
        assertThat(memberA.getNickname()).isEqualTo(memberEntityA.getNickname());
        assertThat(memberAImg.isEmpty()).isEqualTo(true);
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

    SignupDto createSignupDtoImg(){
        MockMultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        SignupDto signupDto = createSignupDto();
        signupDto.setFile(file1);
        return signupDto;
    }

}