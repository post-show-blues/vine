package com.post_show_blues.vine.domain.participant;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParticipantRepositoryTest {

    @Autowired ParticipantRepository participantRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MeetingRepository meetingRepository;
    @Autowired CategoryRepository categoryRepository;

    /*
    @Test
    //TODO 2021.06.03.-@Query로 삭제가 안됨(Repository)-hyeongwoo
    void 모임관련삭제() throws Exception{
        //given
        List<Participant> participantList = createParticipantList();

        Participant participant = participantList.get(0);
        Meeting meeting = participant.getMeeting();

        List<Participant> result = participantRepository.findByMeeting(meeting);

        for(Participant participant1 : result){
            System.out.println("==============================");
            System.out.println(participant1);
        }
        //when

        System.out.println(meeting.getId());

        participantRepository.deleteByMeeting(meeting);

        //then

        List<Participant> result2 = participantRepository.findByMeeting(meeting);

        for(Participant participant2 : result){
            System.out.println("-------------------------------");
            System.out.println(participant2);
            System.out.println(participant2.getMeeting().getId());
        }

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> participantRepository.findById(participant.getId()).get());

        Assertions.assertThat(e.getMessage()).isEqualTo("No value present");


        Member member1 = Member.builder()
                .name("memberA")
                .email("member@kookmin.ac.kr")
                .nickname("memberNickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        Member member2 = Member.builder()
                .name("memberA")
                .email("member@kookmin.ac.kr")
                .nickname("memberNickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        Category category = Category.builder()
                .name("categoryA")
                .build();

        categoryRepository.save(category);

        Meeting meeting = Meeting.builder()
                .category(category)
                .member(member2)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate("2021-06-05")
                .reqDeadline("2021-06-04")
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meeting);

        Participant participant1 = Participant.builder()
                .meeting(meeting)
                .member(member1)
                .req(false)
                .build();

        Participant participant2 = Participant.builder()
                .meeting(meeting)
                .member(member1)
                .req(false)
                .build();

        participantRepository.save(participant1);
        participantRepository.save(participant2);

    }
*/


    private List<Participant> createParticipantList() {
        Meeting meeting = createMeeting();
        Member member = createMember();

        Participant participant1 = Participant.builder()
                .meeting(meeting)
                .member(member)
                .req(false)
                .build();

        Participant participant2 = Participant.builder()
                .meeting(meeting)
                .member(member)
                .req(false)
                .build();

        participantRepository.save(participant1);
        participantRepository.save(participant2);

        List<Participant> participantList = new ArrayList<>();

        participantList.add(participant1);
        participantList.add(participant2);

        return participantList;
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