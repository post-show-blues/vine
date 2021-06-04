package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParticipantServiceImplTest {

    @Autowired ParticipantService participantService;
    @Autowired MeetingRepository meetingRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ParticipantRepository participantRepository;


    @Test
    void 참여요청() throws Exception{
        //given
        Meeting meeting = createMeeting();
        Member member = createMember();

        //when
        Long saveId = participantService.request(meeting.getId(), member.getId());

        //then
        Participant participant = participantService.findOne(saveId);

        Assertions.assertThat(participant.getMeeting()).isEqualTo(meeting);
        Assertions.assertThat(participant.getMember()).isEqualTo(member);
        Assertions.assertThat(participant.getReq()).isEqualTo(false);
    }
    
    @Test
    void 요청시_인원초과() throws Exception{
        //given
        Member member1 = createMember();

        //meeting 생성
        Category category = createCategory();
        Member member2 = createMember();

        Meeting meeting = Meeting.builder()
                .category(category)
                .member(member2)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate("2021-06-05")
                .reqDeadline("2021-06-04")
                .maxNumber(4)
                .currentNumber(4) // maxNumber == currentNumber
                .build();

        meetingRepository.save(meeting);

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> participantService.request(meeting.getId(), member1.getId()));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("참여인원 초과입니다.");
    }

    @Test
    void 참여수락() throws Exception{
        //given
        Meeting meeting = createMeeting();
        Member member = createMember();

        //수락전 참여 현재인원
        int beforeNumber = meeting.getCurrentNumber();

        Participant participant = Participant.builder()
                .meeting(meeting)
                .member(member)
                .build();

        participantRepository.save(participant);

        //when
        participantService.accept(participant.getId());

        //then
        Assertions.assertThat(participant.getReq()).isEqualTo(true);
        Assertions.assertThat(meeting.getCurrentNumber()).isEqualTo(beforeNumber+1);
    }

    @Test
    void 수락시_인원초과() throws Exception{
        //given
        Member member1 = createMember();

        //meeting 생성
        Category category = createCategory();
        Member member2 = createMember();

        Meeting meeting = Meeting.builder()
                .category(category)
                .member(member2)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate("2021-06-05")
                .reqDeadline("2021-06-04")
                .maxNumber(4)
                .currentNumber(4) // maxNumber == currentNumber
                .build();

        meetingRepository.save(meeting);

        //participant 생성
        Participant participant = Participant.builder()
                .member(member1)
                .meeting(meeting)
                .build();

        participantRepository.save(participant);

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> participantService.accept(participant.getId()));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("참여인원 초과입니다.");
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
                .meetDate("2021-06-05")
                .reqDeadline("2021-06-04")
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meeting);

        return meeting;
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

}