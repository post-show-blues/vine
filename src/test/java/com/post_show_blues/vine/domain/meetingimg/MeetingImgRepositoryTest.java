package com.post_show_blues.vine.domain.meetingimg;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MeetingImgRepositoryTest {

    @Autowired MeetingRepository meetingRepository;
    @Autowired MeetingImgRepository meetingImgRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    void 이미지리스트() throws Exception{
        //given
        MeetingImg meetingImg1 = createMeetingImg();
        MeetingImg meetingImg2 = createMeetingImg();
        MeetingImg meetingImg3 = createMeetingImg();

        //when
        List<MeetingImg> result = meetingImgRepository.findAll();

        //then
    }

    private MeetingImg createMeetingImg() {

        Meeting meeting = createMeeting();

        MeetingImg meetingImg = MeetingImg.builder()
                .storeFileName(UUID.randomUUID().toString() + "_MeetingImgA")
                .folderPath("/hyeongwoo1")
                .meeting(meeting)
                .build();

        meetingImgRepository.save(meetingImg);

        return meetingImg;
    }

    private Meeting createMeeting() {

        Category category = createCategory();
        Member member = createMember();

        Meeting meeting = Meeting.builder()
                .category(category)
                .member(member)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2021,06,05,00,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meeting);

        return meeting;
    }

    private Category createCategory () {
        Category category = Category.builder()
                .name("categoryA")
                .build();

        categoryRepository.save(category);

        return category;
    }

    private Member createMember(){
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

}