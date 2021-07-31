package com.post_show_blues.vine.domain.participant;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;


import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ParticipantRepositoryTest {

    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MemberImgRepository memberImgRepository;


    @Test
    void 모임관련삭제() throws Exception {

        //given
        List<Participant> participantList = createParticipantList();

        Participant participant = participantList.get(0);

        Meeting meeting = participant.getMeeting();


        //when
        participantRepository.deleteByMeeting(meeting);

        //then
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> participantRepository.findById(participant.getId()).get());

        Assertions.assertThat(e.getMessage()).isEqualTo("No value present");

        Assertions.assertThat(participantRepository.findByMeeting(meeting).isEmpty()).isTrue();

    }


    @Test
    void 참여자_리스트 () throws Exception {
        //given
        List<Participant> participantList = createParticipantList();

        Participant participant1 = participantList.get(0);

        Long meetingId = participant1.getMeeting().getId();

        //when
        List<Object[]> result = participantRepository.getListParticipantByMeetingId(meetingId);

        //then
        for (Object[] arr : result) {

            System.out.println(Arrays.toString(arr));

        }

        //데이터수 체크
        Assertions.assertThat(result.size())
                .isEqualTo(participantRepository.participantCount(meetingId).intValue());

    }

    private MemberImg createMemberImg () {

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

    private List<Participant> createParticipantList () {
        Meeting meeting = createMeeting();

        MemberImg memberImg1 = createMemberImg();
        Member member1 = memberImg1.getMember();

        MemberImg memberImg2 = createMemberImg();
        Member member2 = memberImg2.getMember();

        MemberImg memberImg3 = createMemberImg();
        Member member3 = memberImg3.getMember();

        Participant participant1 = Participant.builder()
                .meeting(meeting)
                .member(member1)
                .build();

        Participant participant2 = Participant.builder()
                .meeting(meeting)
                .member(member2)
                .build();

        Participant participant3 = Participant.builder()
                .meeting(meeting)
                .member(member3)
                .build();

        participantRepository.save(participant1);
        participantRepository.save(participant2);
        participantRepository.save(participant3);


        List<Participant> participantList = new ArrayList<>();


        participantList.add(participant1);
        participantList.add(participant2);
        participantList.add(participant3);

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
                .meetDate(LocalDateTime.of(2021,06,05,00,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                        LocalDateTime.of(2021,06,05,00,00)
                                .toLocalDate().atStartOfDay()).toDays())
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


