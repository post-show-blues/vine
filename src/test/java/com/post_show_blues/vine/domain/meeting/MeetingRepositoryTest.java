package com.post_show_blues.vine.domain.meeting;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.meetingimg.MeetingImgRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MeetingRepositoryTest {

    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired MemberImgRepository memberImgRepository;
    @Autowired ParticipantRepository participantRepository;
    @Autowired
    MeetingImgRepository meetingImgRepository;

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
                .meetDate(LocalDateTime.of(2021,06,05,14,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                .dDay(Period.between(LocalDate.now(),
                        LocalDateTime.of(2021,06,05,00,00)
                                .toLocalDate()).getDays())
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
    void testGetMeetingWithAll() throws Exception {
        //given

        Meeting meeting = createMeeting();

        MeetingImg meetingImg1 = MeetingImg.builder()
                .meeting(meeting)
                .fileName("MeetingImg1")
                .filePath("/hyeongwoo1")
                .uuid(UUID.randomUUID().toString())
                .build();

        meetingImgRepository.save(meetingImg1);

        MeetingImg meetingImg2 = MeetingImg.builder()
                .meeting(meeting)
                .fileName("MeetingImg2")
                .filePath("/hyeongwoo2")
                .uuid(UUID.randomUUID().toString())
                .build();

        meetingImgRepository.save(meetingImg2);



        //when
        List<Object[]> objects = meetingRepository.getMeetingWithAll(meeting.getId());

        //then
        for (Object[] object : objects){
            System.out.println(Arrays.toString(object));
        }
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

    private MemberImg createMemberImg() {

        Member member = createMember();

        MemberImg memberImg = MemberImg.builder()
                .member(member)
                .fileName("MemberImg1")
                .filePath("/hyeongwoo")
                .uuid(UUID.randomUUID().toString())
                .build();

        memberImgRepository.save(memberImg);

        return memberImg;
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
                .meetDate(LocalDateTime.of(2021,06,05,12,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,14,00))
                .dDay(Period.between(LocalDate.now(),
                        LocalDateTime.of(2021,06,05,00,00)
                                .toLocalDate()).getDays())
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meeting);

        return meeting;

    }
}