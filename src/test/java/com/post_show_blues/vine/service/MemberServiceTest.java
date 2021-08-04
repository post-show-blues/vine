package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.dto.member.FindMemberResultDTO;
import com.post_show_blues.vine.dto.member.MemberUpdateDto;
import com.post_show_blues.vine.dto.memberImg.MemberImgUploadDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    AuthService authService;
    @Autowired
    MemberImgRepository memberImgRepository;

    @Test
    public void 회원정보수정() throws Exception {
        //given
        SignupDto memberEntityA = createSignupDto();
        Object[] join = authService.join(memberEntityA);
        Member memberA = (Member) join[0];
        MemberUpdateDto memberUpdateDto = createMemberUpdateDto();


        //when
        Member updateMember = memberService.memberUpdate(memberA.getId(), memberUpdateDto.toEntity());

        //then
        assertThat(updateMember.getInstaurl()).isEqualTo(memberUpdateDto.getInstaurl());


    }

    @Test
    public void 회원검색() throws Exception {
        //given
        SignupDto memberEntityA = createSignupDto();
        Object[] join = authService.join(memberEntityA);
        Member memberA = (Member) join[0];

        //when
        List<FindMemberResultDTO>  findMemberByNickname = memberService.findMember(memberA.getNickname());
        List<FindMemberResultDTO>  findMemberByEmail = memberService.findMember(memberA.getEmail());

        //then
        boolean isNickname = findMemberByNickname.stream().anyMatch(m -> m.getNickname() == memberA.getNickname());

        assertThat(isNickname).isEqualTo(true);

    }

//    @Test
//    public void 사진수정() throws Exception {
//        //given
//        SignupDto memberEntityA = createSignupDto(); //변경 전 사진
//        Object[] join = authService.join(memberEntityA);
//        Member memberA = (Member) join[0];
//
//        //when
//        MemberImgUploadDto memberImgEntityB = memberImgUploadDtoB(); //변경 후 사진
//        memberService.memberImgUpdate(memberA, Optional.of(memberImgEntityB));
//
//        //then
//        List<MemberImg> memberImgs = memberImgRepository.findAll();
//
//        System.out.println("memberImgs = " + memberImgs);
//
//        //이전 사진
//        boolean isImgA = memberImgs.stream().anyMatch(i -> {
//                    if (i.getFileName() != "")
//                        return i.getFileName().split("_")[1].equals(memberEntityA.getFile().getOriginalFilename());
//                    return false;
//                }
//        );
//
//        //이후 사진
//        boolean isImgB = memberImgs.stream().anyMatch(i -> {
//                    if (i.getFileName() != "")
//                        return i.getFileName().split("_")[1].equals(memberImgEntityB.getFile().getOriginalFilename());
//                    return false;
//                }
//        );
//
//        assertThat(isImgA).isEqualTo(false); //이전사진은 없고
//        assertThat(isImgB).isEqualTo(true); //현재사진은 있다
//
//    }

    SignupDto createSignupDto() {
        MockMultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        return SignupDto.builder()
                .name("memberB")
                .email("member@duksung.ac.kr")
                .nickname("memberNickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("덕성대학교")
                .file(file1)
                .build();
    }

    MemberUpdateDto createMemberUpdateDto() {
        return MemberUpdateDto.builder()
                .text("안녕하세요")
                .instaurl("https://www.instagram.com/dlwlrma/?hl=ko")
                .twitterurl("https://twitter.com/BTS_twt?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor")
                .build();
    }

    MemberImgUploadDto memberImgUploadDtoB() throws IOException {
        MockMultipartFile file1 = new MockMultipartFile("file", "filename-2.jpeg", "image/jpeg", "some-image".getBytes());

        return MemberImgUploadDto.builder()
                .file(file1)
                .build();
    }

}