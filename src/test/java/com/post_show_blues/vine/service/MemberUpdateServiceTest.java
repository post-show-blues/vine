package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.dto.member.MemberUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

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

    Member memberA, memberB;
    MemberImg memberAImg;

    @BeforeEach
    void setUp() throws IOException {
        //사진이 있는 멤버
        Object[] signupDto = createSignupDto();
        memberA = (Member) signupDto[0];
        memberAImg = (MemberImg) signupDto[1];

        //사진이 없는 멤버
        Object[] signupDto2 = createSignupDto2();
        memberB=(Member) signupDto2[0];
    }

    @Test
    public void 회원정보수정() throws Exception {
        //given
        MemberUpdateDto memberUpdateDto = createMemberUpdateDto();

        //when
        Member updateMember = memberUpdateService.memberUpdate(memberA.getId(), memberUpdateDto.toEntity());

        //then
        assertThat(updateMember.getInstaurl()).isEqualTo(memberUpdateDto.getInstaurl());


    }

    @Test
    public void 회원사진수정_DTO_O_DB_O () throws Exception {
        //given
        Optional<MultipartFile> memberImgUploadDto_o = memberImgUploadDto_O();
        memberUpdateService.memberImgUpdate(memberA, memberImgUploadDto_o);

        //when
        MemberImg memberImg = memberImgRepository.findByMember(memberA).get();

        //then
        assertThat(memberImg.getStoreFileName().split("_")[1]).isEqualTo(memberImgUploadDto_o.get().getOriginalFilename());

    }

    @Test
    public void 회원사진수정_DTO_O_DB_X () throws Exception {
        //given
        Optional<MultipartFile> memberImgUploadDto_o = memberImgUploadDto_O();
        memberUpdateService.memberImgUpdate(memberB, memberImgUploadDto_o);

        //when
        MemberImg memberImg = memberImgRepository.findByMember(memberB).get();

        //then
        assertThat(memberImg.getStoreFileName().split("_")[1]).isEqualTo(memberImgUploadDto_o.get().getOriginalFilename());

    }

    @Test
    public void 회원사진수정_DTO_X_DB_O () throws Exception {
        //given
        Optional<MultipartFile> memberImgUploadDto_x = memberImgUploadDto_X();
        memberUpdateService.memberImgUpdate(memberA, memberImgUploadDto_x);

        //when
        boolean empty = memberImgRepository.findByMember(memberA).isEmpty();

        //then
        assertThat(empty).isEqualTo(true);
    }

    @Test
    public void 회원사진수정_DTO_X_DB_X () throws Exception {
        //given
        Optional<MultipartFile> memberImgUploadDto_x = memberImgUploadDto_X();
        memberUpdateService.memberImgUpdate(memberB, memberImgUploadDto_x);

        //when
        boolean empty = memberImgRepository.findByMember(memberB).isEmpty();

        //then
        assertThat(empty).isEqualTo(true);

    }

    Object[] createSignupDto() throws IOException {
        MockMultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        SignupDto signupDTO = SignupDto.builder()
                .name("memberA")
                .email("memberA@duksung.ac.kr")
                .nickname("memberA")
                .password("1111")
                .phone("010-0000-0000")
                .university("덕성대학교")
                .file(file1)
                .build();
        Object[] join = authService.join(signupDTO);

        return join;
    }

    Object[] createSignupDto2() throws IOException {

        SignupDto signupDTO = SignupDto.builder()
                .name("memberB")
                .email("memberB@duksung.ac.kr")
                .nickname("memberB")
                .password("1111")
                .phone("010-0000-0000")
                .university("덕성대학교")
                .build();

        Object[] join = authService.join(signupDTO);

        return join;
    }

    MemberUpdateDto createMemberUpdateDto() {
        return MemberUpdateDto.builder()
                .text("안녕하세요")
                .instaurl("https://www.instagram.com/dlwlrma/?hl=ko")
                .twitterurl("https://twitter.com/BTS_twt?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor")
                .build();
    }

    Optional<MultipartFile> memberImgUploadDto_O() throws IOException {
        return Optional.of(new MockMultipartFile("file", "filename-2.jpeg", "image/jpeg", "some-image".getBytes()));
    }

    Optional<MultipartFile> memberImgUploadDto_X() throws IOException {
        return Optional.ofNullable(null);
    }
}