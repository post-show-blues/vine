package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.follow.FollowRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.dto.member.MemberListDTO;
import com.post_show_blues.vine.dto.member.MemberProfileDTO;
import com.post_show_blues.vine.dto.member.MyProfileDTO;
import com.post_show_blues.vine.service.auth.AuthService;
import com.post_show_blues.vine.service.follow.FollowService;
import com.post_show_blues.vine.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

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
    @Autowired
    FollowService followService;
    @Autowired
    FollowRepository followRepository;

    Member memberA;
    MemberImg memberAImg;

    @BeforeEach
    void setUp() throws IOException {
        Object[] signupDto = createSignupDto();
        memberA = (Member) signupDto[0];
        memberAImg = (MemberImg) signupDto[1];
    }


    @Test
    public void 회원리스트_검색() throws Exception {
        //given
        //when
        List<MemberListDTO> findMemberByNickname = memberService.memberList(memberA.getNickname());

        //then
        boolean isNickname = findMemberByNickname.stream().anyMatch(m -> m.getNickname() == memberA.getNickname());

        assertThat(isNickname).isEqualTo(true);

    }

    @Test
    public void 회원프로필_조회_팔로우x() throws Exception {
        //given
        Object[] signupDto = createSignupDto2();
        Member memberB = (Member) signupDto[0];
        MemberImg memberBImg = (MemberImg) signupDto[1];

        //when
        //멤버A가 멤버 B의 프로필 검색
        MemberProfileDTO memberProfileDTO = memberService.memberProfile(memberA.getId(), memberB.getId());

        //then
        assertThat(memberProfileDTO.getIsFollow()).isEqualTo(false);
        assertThat(memberProfileDTO.getNickname()).isEqualTo(memberB.getNickname());

    }

    @Test
    public void 회원프로필_조회_팔로우o() throws Exception {
        //given
        Object[] signupDto = createSignupDto2();
        Member memberB = (Member) signupDto[0];
        MemberImg memberBImg = (MemberImg) signupDto[1];

        followService.isFollow(memberA.getId(), memberB.getId());

        //when
        //멤버A가 멤버 B의 프로필 검색 && 멤버A는 멤버 B를 팔로우
        MemberProfileDTO memberProfileDTO = memberService.memberProfile(memberA.getId(), memberB.getId());

        System.out.println(memberProfileDTO);
        //then
        assertThat(memberProfileDTO.getIsFollow()).isEqualTo(true);
        assertThat(memberProfileDTO.getNickname()).isEqualTo(memberB.getNickname());

    }

    @Test
    public void 내프로필_조회() throws Exception {
        //given
        //when //멤버A가 자신의 프로필 조회
        MyProfileDTO myProfileDTO = memberService.MyProfile(memberA.getId());

        //then
        assertThat(myProfileDTO.getId()).isEqualTo(memberA.getId());
        assertThat(myProfileDTO.getNickname()).isEqualTo(memberA.getNickname());

    }

    Object[] createSignupDto() throws IOException {
        MockMultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        SignupDto signupDTO = SignupDto.builder()
                .name("memberA")
                .email("member@duksung.ac.kr")
                .nickname("memberNickname")
                .password("1111")
                .phone("010-0000-0000")
                .file(file1)
                .build();
        Object[] join = authService.join(signupDTO);

        return join;
    }

    Object[] createSignupDto2() throws IOException {
        MockMultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        SignupDto signupDTO = SignupDto.builder()
                .name("memberB")
                .email("memberB@duksung.ac.kr")
                .nickname("memberB")
                .password("1111")
                .phone("010-0000-0000")
                .file(file1)
                .build();
        Object[] join = authService.join(signupDTO);

        return join;
    }


}