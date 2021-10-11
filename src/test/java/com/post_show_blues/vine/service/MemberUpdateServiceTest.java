package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.dto.auth.SignupResponse;
import com.post_show_blues.vine.dto.member.MemberUpdateDto;
import com.post_show_blues.vine.service.auth.AuthService;
import com.post_show_blues.vine.service.member.MemberService;
import com.post_show_blues.vine.service.member.MemberUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberUpdateServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberUpdateService memberUpdateService;
    @Autowired
    AuthService authService;
    @Autowired
    MemberImgRepository memberImgRepository;
    @Autowired
    MemberRepository memberRepository;

    Member memberA, memberB;
    MemberImg memberAImg;

    @BeforeEach
    void setUp() throws IOException {
        //사진이 있는 멤버
        SignupResponse signupDto = createSignupDto();
        memberA = memberRepository.findById(signupDto.getId()).get();
        memberAImg = memberImgRepository.findByMember(memberA).get();

        //사진이 없는 멤버
        SignupResponse signupDto2 = createSignupDto2();
        memberB=memberRepository.findById(signupDto2.getId()).get();
    }

    @Test
    public void 회원정보수정() throws Exception {
        //given
        MemberUpdateDto memberUpdateDto = createMemberUpdateDto();

        //when
        Member updateMember = memberUpdateService.memberUpdate(memberA.getId(), memberUpdateDto);

        //then
        assertThat(updateMember.getInstaurl()).isEqualTo(memberUpdateDto.getInstaurl());


    }

    @Test
    public void 회원사진수정_DTO_O_DB_O () throws Exception {
        //given
        MemberUpdateDto memberUpdateDto = createMemberUpdateDto();
        memberUpdateDto.setFile(memberImgUploadDto_O());

        memberUpdateService.memberUpdate(memberA.getId(), memberUpdateDto);

        //when
        MemberImg memberImg = memberImgRepository.findByMember(memberA).get();

        //then
        assertThat(memberImg.getStoreFileName().split("_")[1]).isEqualTo(memberUpdateDto.getFile().getOriginalFilename());

    }

    @Test
    public void 회원사진수정_DTO_O_DB_X () throws Exception { //이름이 동일함
        //given
        MemberUpdateDto memberUpdateDto = createMemberUpdateDto();
        memberUpdateDto.setFile(memberImgUploadDto_O());

        memberUpdateService.memberUpdate(memberB.getId(), memberUpdateDto);

        //when
        MemberImg memberImg = memberImgRepository.findByMember(memberB).get();

        //then
        assertThat(memberImg.getStoreFileName().split("_")[1]).isEqualTo(memberUpdateDto.getFile().getOriginalFilename());

    }

    @Test
    public void 회원사진수정_DTO_X_DB_O () throws Exception {
        //given
        MemberUpdateDto memberUpdateDto = createMemberUpdateDto();
        memberUpdateDto.setFile(memberImgUploadDto_X());

        memberUpdateService.memberUpdate(memberA.getId(), memberUpdateDto);

        //when
        boolean empty = memberImgRepository.findByMember(memberA).isEmpty();

        //then
        assertThat(empty).isEqualTo(true);
    }

    @Test
    public void 회원사진수정_DTO_X_DB_X () throws Exception {
        //given
        MemberUpdateDto memberUpdateDto = createMemberUpdateDto();
        memberUpdateDto.setFile(memberImgUploadDto_X());
        memberUpdateService.memberUpdate(memberB.getId(), memberUpdateDto);

        //when
        boolean empty = memberImgRepository.findByMember(memberB).isEmpty();

        //then
        assertThat(empty).isEqualTo(true);

    }

    SignupResponse createSignupDto() throws IOException {
        MockMultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        SignupDto signupDTO = SignupDto.builder()
                .email("memberA@duksung.ac.kr")
                .nickname("memberA")
                .password("1111")
                .file(file1)
                .build();
        SignupResponse join = authService.join(signupDTO);

        return join;
    }

    SignupResponse createSignupDto2() throws IOException {

        SignupDto signupDTO = SignupDto.builder()
                .email("memberB@duksung.ac.kr")
                .nickname("memberB")
                .password("1111")
                .build();

        SignupResponse join = authService.join(signupDTO);

        return join;
    }

    MemberUpdateDto createMemberUpdateDto() {
        return MemberUpdateDto.builder()
                .text("안녕하세요")
                .instaurl("https://www.instagram.com/dlwlrma/?hl=ko")
                .facebookurl("https://twitter.com/BTS_twt?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor")
                .build();
    }

    MultipartFile memberImgUploadDto_O() throws IOException {
        return new MockMultipartFile("file", "filename-2.jpeg", "image/jpeg", "some-image".getBytes());
    }

    MultipartFile memberImgUploadDto_X() throws IOException {
        return null;
    }
}