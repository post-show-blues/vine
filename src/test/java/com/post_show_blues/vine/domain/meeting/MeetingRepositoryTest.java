package com.post_show_blues.vine.domain.meeting;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MeetingRepositoryTest {

    @Autowired MeetingRepository meetingRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberImgRepository memberImgRepository;
    @Autowired ParticipantRepository participantRepository;

    @Test
    void testGetListPage() throws Exception {
        //given
        MemberImg memberImg1 = createMemberImg();

        Member member1 = memberImg1.getMember();

        //meeting 생성
        Category category = createCategory();

        Meeting meeting = Meeting.builder()
                .category(category)
                .member(member1)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate("2021-06-05")
                .reqDeadline("2021-06-04")
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meeting);

        //participant1 생성

        MemberImg memberImg2 = createMemberImg();

        Member member2 = memberImg2.getMember();

        Participant participant1 = Participant.builder()
                .meeting(meeting)
                .member(member2)
                .build();

        participantRepository.save(participant1);

        //participant2 생성

        MemberImg memberImg3 = createMemberImg();

        Member member3 = memberImg3.getMember();

        Participant participant2 = Participant.builder()
                .meeting(meeting)
                .member(member3)
                .build();

        participantRepository.save(participant2);

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("meeting_id").descending());

        //when
        Page<Object[]> result = meetingRepository.getListPage(pageRequest);

        //then
        System.out.println(result.getTotalElements());
        System.out.println(result.getTotalPages());
        System.out.println(result.getClass());
        System.out.println(result.get());

        for (Object[] objects : result.getContent()) {
            System.out.println(Arrays.toString(objects));
        }

    }

    @Test
    void date타입() throws Exception{
        Category category = createCategory();
        Member member = createMember();

        Meeting meeting = Meeting.builder()
                .category(category)
                .member(member)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate("2021-06-05")
                .reqDeadline("2021-06-04")
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meeting);

        String deadline = meeting.getReqDeadline();


    }


    private Category createCategory() {
        Category category = Category.builder()
                .name("categoryA")
                .build();

        categoryRepository.save(category);

        return category;
    }

    private Member createMember() {
        Member member = Member.builder()
                .name("memberA")
                .email("member@kookmin.ac.kr")
                .nickname("memberNickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(member);

        return member;
    }

    private MemberImg createMemberImg(){

        Member member = createMember();

        MemberImg memberImg = MemberImg.builder()
                .member(member)
                .fileName("Img1")
                .filePath("/hyeongwoo")
                .uuid(UUID.randomUUID().toString())
                .build();

        memberImgRepository.save(memberImg);

        return memberImg;
    }

}