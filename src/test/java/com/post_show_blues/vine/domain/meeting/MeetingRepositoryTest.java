package com.post_show_blues.vine.domain.meeting;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.meetingimg.MeetingImgRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
@Transactional
class MeetingRepositoryTest {

    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired MemberImgRepository memberImgRepository;
    @Autowired ParticipantRepository participantRepository;
    @Autowired
    MeetingImgRepository meetingImgRepository;


    @Test
    void testGetMeetingWithAll() throws Exception {
        //given

        Meeting meeting = createMeeting();

        MeetingImg meetingImg1 = MeetingImg.builder()
                .meeting(meeting)
                .storeFileName(UUID.randomUUID().toString() + "_MeetingImg1")
                .folderPath("/hyeongwoo1")
                .build();

        meetingImgRepository.save(meetingImg1);

        MeetingImg meetingImg2 = MeetingImg.builder()
                .meeting(meeting)
                .storeFileName(UUID.randomUUID().toString() + "_MeetingImg2")
                .folderPath("/hyeongwoo1")
                .build();

        meetingImgRepository.save(meetingImg2);



        //when
        List<Object[]> objects = meetingRepository.getMeetingWithAll(meeting.getId());

        //then
        for (Object[] object : objects){
            System.out.println(Arrays.toString(object));
        }
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

    private MemberImg createMemberImg() {

        Member member = createMember();

        MemberImg memberImg = MemberImg.builder()
                .member(member)
                .folderPath("vine/2021/09/21")
                .storeFileName("231dfsaf@Rfl_file1.jpeg")
                .build();

        memberImgRepository.save(memberImg);

        return memberImg;
    }


    private Meeting createMeeting() {
        Member member = createMember();

        Meeting meeting = Meeting.builder()
                .category(Category.SPORTS)
                .member(member)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2021,06,05,12,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,14,00))
                .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                        LocalDateTime.of(2021,06,05,00,00)
                                .toLocalDate().atStartOfDay()).toDays())
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meeting);

        return meeting;

    }
}