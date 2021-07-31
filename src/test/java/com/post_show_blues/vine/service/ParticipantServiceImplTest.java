package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.dto.participant.ParticipantDTO;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParticipantServiceImplTest {

    @Autowired ParticipantService participantService;
    @Autowired MeetingRepository meetingRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ParticipantRepository participantRepository;
    @Autowired MemberImgRepository memberImgRepository;
    @Autowired NoticeRepository noticeRepository;

    @Test
    void 추방_기능() throws Exception{
        //given
        Member member =Member.builder()
                .name("member")
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();
        memberRepository.save(member);

        Meeting meeting = createMeeting();
        int beforeNumber = meeting.getCurrentNumber();

        Participant participant = Participant.builder()
                .meeting(meeting)
                .member(member)
                .build();

        participantRepository.save(participant);

        //when
        //추방일때
        participantService.remove(participant.getId(), meeting.getMember().getId()); //로그인 ID = masterId

        //then
        Assertions.assertThat(meeting.getCurrentNumber()).isEqualTo(beforeNumber-1);

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> participantRepository.findById(participant.getId()).get());

        Assertions.assertThat(e.getMessage()).isEqualTo("No value present");

        //알림 검증 -> participantMember에게 알림 생성
        List<Notice> noticeList = noticeRepository.getNoticeList(member.getId());

        Assertions.assertThat(noticeList.size()).isEqualTo(1);

        for (Notice notice : noticeList){
            System.out.println(notice.toString());
        }

    }

    @Test
    void 나가기_기능() throws Exception{
        //given
        Member member =Member.builder()
                .name("member")
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();
        memberRepository.save(member);

        Meeting meeting = createMeeting();
        int beforeNumber = meeting.getCurrentNumber();

        Participant participant = Participant.builder()
                .meeting(meeting)
                .member(member)
                .build();

        participantRepository.save(participant);

        //when
        //나가기 경우
        participantService.remove(participant.getId(), member.getId()); //로그인 ID = participantMemberId

        //then
        Assertions.assertThat(meeting.getCurrentNumber()).isEqualTo(beforeNumber-1);

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> participantRepository.findById(participant.getId()).get());

        Assertions.assertThat(e.getMessage()).isEqualTo("No value present");

        //알림 검증 -> master 에게  알림 생성
        List<Notice> noticeList = noticeRepository.getNoticeList(meeting.getMember().getId());

        Assertions.assertThat(noticeList.size()).isEqualTo(1);

        for (Notice notice : noticeList){
            System.out.println(notice.toString());
        }
    }

    @Test
    void 참여자_리스트() throws Exception{
        //given

        List<Participant> participantList = createParticipantList();

        Participant participant = participantList.get(0);

        Long meetingId = participant.getMeeting().getId();

        //when
        List<ParticipantDTO> result = participantService.getParticipantList(meetingId);

        //then

        //데이터수 체크
        Assertions.assertThat(result.size())
                .isEqualTo(participantRepository.participantCount(meetingId).intValue());
    }

    private List<Participant> createParticipantList() {

        Meeting meeting = createMeeting();

        Member member1 =Member.builder()
                .name("member")
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();
        memberRepository.save(member1);

        MemberImg memberImg2 = createMemberImg();
        Member member2 = memberImg2.getMember();

        //member3 만들기
        Member memberC =Member.builder()
                .name("member")
                .email("memberC@kookmin.ac.kr")
                .nickname("memberCNickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();
        memberRepository.save(memberC);

        MemberImg memberImg3 = MemberImg.builder()
                .member(memberC)
                .fileName("MemberImg1")
                .filePath("/hyeongwoo")
                .uuid(UUID.randomUUID().toString())
                .build();

        memberImgRepository.save(memberImg3);

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

    private MemberImg createMemberImg() {

        Member memberB =Member.builder()
                .name("member")
                .email("memberB@kookmin.ac.kr")
                .nickname("memberBNickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();
        memberRepository.save(memberB);

        MemberImg memberImg = MemberImg.builder()
                .member(memberB)
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