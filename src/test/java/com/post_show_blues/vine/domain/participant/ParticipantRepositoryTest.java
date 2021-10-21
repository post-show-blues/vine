package com.post_show_blues.vine.domain.participant;

import com.post_show_blues.vine.domain.category.Category;
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

    @Test
    void 참가_존재여부_존재o() throws Exception{
        //given
        //모임생성
        Meeting meeting = createMeeting();
        //회원 생성
        Member memberE =Member.builder()
                .email("memberE@kookmin.ac.kr")
                .nickname("memberENickname")
                .password("1111")
                .build();
        memberRepository.save(memberE);

        Participant participant = Participant.builder()
                .meeting(meeting)
                .member(memberE)
                .build();
        participantRepository.save(participant);

        //when
        boolean result = participantRepository.existsByMeetingAndMember(meeting, memberE);

        //then
        Assertions.assertThat(result).isTrue();
    }

    private MemberImg createMemberImg () {

        Member memberC = Member.builder()
//                .name("member")
                .email("memberC@kookmin.ac.kr")
                .nickname("memberCNickname")
                .password("1111")
//                .phone("010-0000-0000")
//                .university("국민대학교")
                .build();

        memberRepository.save(memberC);

        MemberImg memberImg = MemberImg.builder()
                .member(memberC)
                .folderPath("vine/2021/09/21")
                .storeFileName("231saf@Rfl_file1.jpeg")
                .build();

        memberImgRepository.save(memberImg);

        return memberImg;
    }

    private List<Participant> createParticipantList () {
        Meeting meeting = createMeeting();

        Member memberB =Member.builder()
//                .name("memberB")
                .email("memberB@kookmin.ac.kr")
                .nickname("memberBNickname")
                .password("1111")
//                .phone("010-0000-0000")
//                .university("국민대학교")
                .build();
        memberRepository.save(memberB);

        MemberImg memberImg = createMemberImg();
        Member memberC = memberImg.getMember();

        //memberD 만들기
        Member memberD =Member.builder()
//                .name("member")
                .email("memberD@kookmin.ac.kr")
                .nickname("memberDNickname")
                .password("1111")
//                .phone("010-0000-0000")
//                .university("국민대학교")
                .build();
        memberRepository.save(memberD);

        MemberImg memberImg2 = MemberImg.builder()
                .member(memberD)
                .folderPath("vine/2021/09/21")
                .storeFileName("dddf@Rfl_file1.jpeg")
                .build();

        memberImgRepository.save(memberImg2);


        Participant participant1 = Participant.builder()
                .meeting(meeting)
                .member(memberB)
                .build();

        Participant participant2 = Participant.builder()
                .meeting(meeting)
                .member(memberC)
                .build();

        Participant participant3 = Participant.builder()
                .meeting(meeting)
                .member(memberD)
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
        Member member = createMember();

        Meeting meeting = Meeting.builder()
                .category(Category.SPORTS)
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


    private Member createMember(){
        Member member = Member.builder()
                .email("memberA@kookmin.ac.kr" )
                .nickname("memberANickname")
                .password("1111")
                .build();

        memberRepository.save(member);

        return member;
    }

}


