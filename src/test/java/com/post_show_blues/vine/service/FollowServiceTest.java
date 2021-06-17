package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.follow.Follow;
import com.post_show_blues.vine.domain.follow.FollowRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FollowServiceTest {

    @Autowired
    FollowService followService;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 팔로우() throws Exception {
        //given
        //인원 두 명 만들어주기
        Member memberA = memberA();
        Member memberB = memberB();
        followRepository.rFollow(memberA.getId(), memberB.getId());

        //when
        List<Follow> followList = followRepository.findAll();
        Follow follow = followList.get(0);

        System.out.println("follow = " + follow);
        System.out.println(followList.contains(follow));

        //then
        assertThat(follow.getFromMemberId().getId()).isEqualTo(memberA.getId());
        assertThat(follow.getToMemberId().getId()).isEqualTo(memberB.getId());
    }

    @Test
    public void 언팔로우() throws Exception {
        //given
        Member memberA = memberA();
        Member memberB = memberB();
        followRepository.rFollow(memberA.getId(), memberB.getId());
        List<Follow> follows = followRepository.findAll();
        Follow follow = follows.get(follows.size()-1);

        //when
        followRepository.rUnFollow(memberA.getId(), memberB.getId());
        List<Follow> unFollow = followRepository.findAll();

        unFollow.contains(follow);
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