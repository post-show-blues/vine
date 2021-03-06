package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.meetingimg.MeetingImgRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.domain.requestParticipant.RequestParticipant;
import com.post_show_blues.vine.domain.requestParticipant.RequestParticipantRepository;
import com.post_show_blues.vine.dto.requestParticipant.RequestParticipantDTO;
import com.post_show_blues.vine.handler.exception.CustomException;
import com.post_show_blues.vine.service.requestParticipant.RequestParticipantService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class RequestParticipantServiceImplTest {

    @Autowired RequestParticipantService requestParticipantService;
    @Autowired MeetingRepository meetingRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired RequestParticipantRepository requestParticipantRepository;
    @Autowired MemberImgRepository memberImgRepository;
    @Autowired MeetingImgRepository meetingImgRepository;
    @Autowired ParticipantRepository participantRepository;
    @Autowired NoticeRepository noticeRepository;

    @Test
    void ????????????() throws Exception{
        //given
        Meeting meeting = createMeeting();
        Member member =Member.builder()
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
                .build();

        memberRepository.save(member);

        //when
        Long saveId = requestParticipantService.request(meeting.getId(), member.getId());

        //then
        RequestParticipant requestParticipant = requestParticipantService.findOne(saveId);

        Assertions.assertThat(requestParticipant.getMeeting()).isEqualTo(meeting);
        Assertions.assertThat(requestParticipant.getMember()).isEqualTo(member);

        //?????? ?????? -> master ?????? ?????? ??????
        List<Notice> noticeList = noticeRepository.getNoticeList(meeting.getMember().getId());
        Assertions.assertThat(noticeList.size()).isEqualTo(1);

        for(Notice notice : noticeList){
            System.out.println(notice.toString());
        }

    }


    /* //?????????????????? ????????????
    @Test
    void ?????????_???????????????() throws Exception{
        //given
        Member member1 = createMember();

        //meeting ??????
        Category category = createCategory();
        Member member2 = createMember();

        Meeting meeting = Meeting.builder()
                .category(category)
                .member(member2)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate("2021-06-13")
                .reqDeadline("2021-06-12") //?????? ?????? -1
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meeting);

        //when
        CustomException e = assertThrows(CustomException.class,
                () -> participantService.request(meeting.getId(), member1.getId()));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("?????? ???????????? ???????????????.");
    }
     */

    @Test
    void ????????????_????????????o() throws Exception{
        //given

        //????????????
        Meeting meeting = createMeeting();

        //????????????
        Member member =Member.builder()
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
                .build();
        memberRepository.save(member);

        //???????????? ??????
        RequestParticipant requestParticipant = RequestParticipant.builder()
                .meeting(meeting)
                .member(member)
                .build();
        requestParticipantRepository.save(requestParticipant);

        //when
        CustomException e = assertThrows(CustomException.class,
                () -> requestParticipantService.request(meeting.getId(), member.getId()));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("?????? ?????? ??????????????????.");
    }

    @Test
    void ?????????_????????????() throws Exception{
        //given
        Member member1 = createMember();

        //meeting ??????
        Member member2 =Member.builder()
//                .name("member")
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
//                .phone("010-0000-0000")
//                .university("???????????????")
                .build();
        memberRepository.save(member2);

        Meeting meeting = Meeting.builder()
                .category(Category.SPORTS)
                .member(member2)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2021,06,05,00,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                        LocalDateTime.of(2021,06,05,00,00)
                                .toLocalDate().atStartOfDay()).toDays())
                .maxNumber(4)
                .currentNumber(4) // maxNumber == currentNumber
                .build();

        meetingRepository.save(meeting);

        //when
        CustomException e = assertThrows(CustomException.class,
                () -> requestParticipantService.request(meeting.getId(), member1.getId()));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("???????????? ???????????????.");
    }

    @Test
    void ????????????() throws Exception{
        //given
        Meeting meeting = createMeeting();
        Member member =Member.builder()
//                .name("member")
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
//                .phone("010-0000-0000")
//                .university("???????????????")
                .build();
        memberRepository.save(member);

        //????????? ?????? ????????????
        int beforeNumber = meeting.getCurrentNumber();

        RequestParticipant requestParticipant = RequestParticipant.builder()
                .meeting(meeting)
                .member(member)
                .build();

        requestParticipantRepository.save(requestParticipant);

        //when
        requestParticipantService.accept(requestParticipant.getId(), meeting.getMember().getId());

        //then
        //?????? ???????????? ???????????? ??????
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> requestParticipantRepository.findById(requestParticipant.getId()).get());
        //????????? ????????? ??????
        Assertions.assertThat(participantRepository
                .findByMeetingIdMemberId(meeting.getId(), member.getId()).isPresent())
                .isTrue();
        //Meeting ?????? ?????? +1
        Assertions.assertThat(meeting.getCurrentNumber()).isEqualTo(beforeNumber+1);

        //?????? ?????? -> ??????????????? ??????
        List<Notice> noticeList = noticeRepository.getNoticeList(member.getId());

        Assertions.assertThat(noticeList.size()).isEqualTo(1);
        for (Notice notice : noticeList){
            System.out.println(notice.toString());
        }

    }

    @Test
    void ????????????_??????x() throws Exception{
        //given
        //?????? ??????
        Meeting meeting = createMeeting();

        //????????? ????????? ??????
        Member member =Member.builder()
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
                .build();
        memberRepository.save(member);

        //?????? ?????? ??????
        RequestParticipant requestParticipant = RequestParticipant.builder()
                .meeting(meeting)
                .member(member)
                .build();

        requestParticipantRepository.save(requestParticipant);

        //when
        //?????? ???????????? ?????? (?????? ???????????? ????????? x)
        CustomException e = assertThrows(CustomException.class,
                () -> requestParticipantService.accept(requestParticipant.getId(), member.getId()));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("???????????? ????????? ????????????.");
    }

    @Test
    void ?????????_????????????() throws Exception{
        //given
        Member member1 = createMember();

        //meeting ??????
        Member member2 =Member.builder()
//                .name("member")
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
//                .phone("010-0000-0000")
//                .university("???????????????")
                .build();
        memberRepository.save(member2);

        Meeting meeting = Meeting.builder()
                .category(Category.SPORTS)
                .member(member2)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2021,06,05,00,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                        LocalDateTime.of(2021,06,05,00,00)
                                .toLocalDate().atStartOfDay()).toDays())
                .maxNumber(4)
                .currentNumber(4) // maxNumber == currentNumber
                .build();

        meetingRepository.save(meeting);

        //requestParticipant ??????
        RequestParticipant requestParticipant = RequestParticipant.builder()
                .member(member1)
                .meeting(meeting)
                .build();

        requestParticipantRepository.save(requestParticipant);

        //when
        CustomException e = assertThrows(CustomException.class,
                () -> requestParticipantService.accept(requestParticipant.getId(), meeting.getMember().getId()));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("???????????? ???????????????.");
    }

    @Test
    void ??????????????????() throws Exception{
        //given
        Meeting meeting = createMeeting();
        Member member =Member.builder()
//                .name("member")
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
//                .phone("010-0000-0000")
//                .university("???????????????")
                .build();
        memberRepository.save(member);

        RequestParticipant requestParticipant = RequestParticipant.builder()
                .member(member)
                .meeting(meeting)
                .build();

        requestParticipantRepository.save(requestParticipant);

        //when
        requestParticipantService.reject(requestParticipant.getId(), meeting.getMember().getId());

        //then
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> requestParticipantRepository.findById(requestParticipant.getId()).get());

        Assertions.assertThat(e.getMessage()).isEqualTo("No value present");

        //?????? ?????? -> ?????? ?????? ????????????
        List<Notice> noticeList = noticeRepository.getNoticeList(member.getId());

        Assertions.assertThat(noticeList.size()).isEqualTo(1);

        for (Notice notice : noticeList){
            System.out.println(notice.toString());
        }
    }

    @Test
    void ????????????_??????_??????_??????x() throws Exception{
        //given
        //?????? ??????
        Meeting meeting = createMeeting();

        //?????? ????????? ??????
        Member member =Member.builder()
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
                .build();
        memberRepository.save(member);

        //?????? ?????? ??????
        RequestParticipant requestParticipant = RequestParticipant.builder()
                .member(member)
                .meeting(meeting)
                .build();

        requestParticipantRepository.save(requestParticipant);

        //?????? ??????_?????? ?????? ?????? ?????? ??????
        Member memberX =Member.builder()
                .email("memberX@kookmin.ac.kr")
                .nickname("memberXNickname")
                .password("1111")
                .build();
        memberRepository.save(memberX);

        //when
        //?????? ?????? ??????_?????? ????????? ?????? ????????? ??????_????????? ??????
        CustomException e = assertThrows(CustomException.class,
                () -> requestParticipantService.accept(requestParticipant.getId(), memberX.getId()));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("???????????? ????????? ????????????.");
    }

    @Test
    void ?????????_?????????(){

        //given
        List<RequestParticipant> requestParticipantList = createRequestParticipantList();

        //meetingId ????????????
        RequestParticipant requestParticipant = requestParticipantList.get(0);
        Long meetingId = requestParticipant.getMeeting().getId();

        //when
        List<RequestParticipantDTO> result =
                requestParticipantService.getRequestParticipantList(meetingId);

        //then
        Assertions.assertThat(result.size())
                .isEqualTo(requestParticipantRepository.requestParticipantCount(meetingId).intValue());



    }

    private List<RequestParticipant> createRequestParticipantList() {

        Meeting meeting = createMeeting();

        //member1 ?????????
        Member member1 =Member.builder()
//                .name("member")
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
//                .phone("010-0000-0000")
//                .university("???????????????")
                .build();
        memberRepository.save(member1);

        //member2 ?????????
        MemberImg memberImg2 = createMemberImg();
        Member member2 = memberImg2.getMember();

        //member3 ?????????
        Member memberC =Member.builder()
//                .name("member")
                .email("memberC@kookmin.ac.kr")
                .nickname("memberCNickname")
                .password("1111")
//                .phone("010-0000-0000")
//                .university("???????????????")
                .build();
        memberRepository.save(memberC);

        MemberImg memberImg3 = MemberImg.builder()
                .member(memberC)
                .folderPath("vine/2021/09/21")
                .storeFileName("231!!!l_file1.jpeg")
                .build();

        memberImgRepository.save(memberImg3);

        Member member3 = memberImg3.getMember();


        RequestParticipant requestParticipant1 = RequestParticipant.builder()
                .meeting(meeting)
                .member(member1)
                .build();

        RequestParticipant requestParticipant2 = RequestParticipant.builder()
                .meeting(meeting)
                .member(member2)
                .build();

        RequestParticipant requestParticipant3 = RequestParticipant.builder()
                .meeting(meeting)
                .member(member3)
                .build();

        requestParticipantRepository.save(requestParticipant1);
        requestParticipantRepository.save(requestParticipant2);
        requestParticipantRepository.save(requestParticipant3);

        List<RequestParticipant> requestParticipantList = new ArrayList<>();

        requestParticipantList.add(requestParticipant1);
        requestParticipantList.add(requestParticipant2);
        requestParticipantList.add(requestParticipant3);

        return requestParticipantList;

    }

    private MemberImg createMemberImg() {

        Member memberB =Member.builder()
//                .name("member")
                .email("memberB@kookmin.ac.kr")
                .nickname("memberBNickname")
                .password("1111")
//                .phone("010-0000-0000")
//                .university("???????????????")
                .build();
        memberRepository.save(memberB);

        MemberImg memberImg = MemberImg.builder()
                .member(memberB)
                .folderPath("vine/2021/09/21")
                .storeFileName("23aa1f@Rfl_file1.jpeg")
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


    private Member createMember() {
        Member member = Member.builder()
//                .name("memberA")
                .email("member@kookmin.ac.kr")
                .nickname("memberNickname")
                .password("1111")
//                .phone("010-0000-0000")
//                .university("???????????????")
                .build();

        memberRepository.save(member);

        return member;
    }

}