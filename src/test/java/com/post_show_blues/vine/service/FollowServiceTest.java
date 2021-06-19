package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.follow.Follow;
import com.post_show_blues.vine.domain.follow.FollowRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
public class FollowServiceTest {

    @Autowired
    FollowService followService;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    NoticeRepository noticeRepository;

    Member memberA;
    Member memberB;


    @BeforeEach
    public void setup(){
        memberA = memberA();
        memberB = memberB();
        followService.isFollow(memberA.getId(), memberB.getId());
    }


    @Test
    public void 팔로우() throws Exception {
        //given
        //setup()에서 함
        System.out.println("memberA = " + memberA);

        //when
        List<Follow> followList = followRepository.findAll();
        Follow follow = followList.get(followList.size()-1);

        System.out.println("follow = " + follow);
        System.out.println(followList.contains(follow));

        //then
        assertThat(follow.getFromMemberId().getId()).isEqualTo(memberA.getId());
        assertThat(follow.getToMemberId().getId()).isEqualTo(memberB.getId());
    }

    @Test
    public void 알람테이블() throws Exception {
        //given
        //setup()에서 함

        //when
        List<Notice> noticeList = noticeRepository.findAll();
        Notice notice = noticeList.get(noticeList.size() - 1);

        //then
        assertThat(notice.getMemberId()).isEqualTo(memberB.getId());

    }


    @Test
    public void 언팔로우() throws Exception {
        //given
        System.out.println("memberA = " + memberA);
        List<Follow> followList = followRepository.findAll();
        Follow follow = followList.get(followList.size()-1);

        //when
        followService.isUnFollow(memberA.getId(), memberB.getId());
        List<Follow> unFollow = followRepository.findAll();

        //then
        assertThat(unFollow.contains(follow)).isEqualTo(false);
    }

    Member memberA() {
        Member memberA = Member.builder()
                .name("memberA")
                .email("member@duksung.ac.kr")
                .nickname("memberNickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("덕성대학교")
                .build();
        memberRepository.save(memberA);
        return memberA;
    }

    Member memberB() {
        Member memberB = Member.builder()
                .name("memberB")
                .email("member@kookmin.ac.kr")
                .nickname("Nickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(memberB);
        return memberB;
    }


}