package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.follow.FollowRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.dto.auth.SignupResponse;
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
    @Autowired
    MemberRepository memberRepository;

    Member memberA;
    MemberImg memberAImg;

    @BeforeEach
    void setUp() throws IOException {
        SignupResponse signupDto = createSignupDto();
        memberA = memberRepository.findById(signupDto.getId()).get();
        memberAImg = memberImgRepository.findByMember(memberA).get();
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
        SignupResponse memberB = createSignupDto2();

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
        SignupResponse memberB = createSignupDto2();

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
        MyProfileDTO myProfileDTO = memberService.myProfile(memberA.getId());

        //then
        assertThat(myProfileDTO.getId()).isEqualTo(memberA.getId());
        assertThat(myProfileDTO.getNickname()).isEqualTo(memberA.getNickname());

    }

    SignupResponse createSignupDto() throws IOException {
        MockMultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        SignupDto signupDTO = SignupDto.builder()
                .email("member@duksung.ac.kr")
                .nickname("memberNickname")
                .password("1111")
                .file(file1)
                .build();
        SignupResponse join = authService.join(signupDTO);

        return join;
    }

    SignupResponse createSignupDto2() throws IOException {
        MockMultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        SignupDto signupDTO = SignupDto.builder()
                .email("memberB@duksung.ac.kr")
                .nickname("memberB")
                .password("1111")
                .file(file1)
                .build();
        SignupResponse join = authService.join(signupDTO);

        return join;
    }


}