package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.dto.notice.NoticeDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.service.notice.NoticeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@Transactional
class NoticeServiceImplTest {

    @Autowired NoticeService noticeService;
    @Autowired NoticeRepository noticeRepository;
    @Autowired MeetingRepository meetingRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ParticipantRepository participantRepository;

    @Test
    void dDay_알림생성() throws Exception{
        //given
        Meeting meetingA = createMeeting(); //dDay = 1

        //meetingA 모임 participant 생성 -> participant1(memberB), participant2(memberC)
        Member memberB =Member.builder()
                .name("memberB")
                .email("memberB@kookmin.ac.kr")
                .nickname("memberNicknameB")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(memberB);

        Participant participant1 = Participant.builder()
                .meeting(meetingA)
                .member(memberB)
                .build();

        participantRepository.save(participant1);

        Member memberC =Member.builder()
                .name("memberC")
                .email("memberC@kookmin.ac.kr")
                .nickname("memberNicknameC")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(memberC);

        Participant participant2 = Participant.builder()
                .meeting(meetingA)
                .member(memberC)
                .build();

        participantRepository.save(participant2);

        //dDay = 0 이 아닌 meeting 생성 -> meetingB
        Category category = createCategory();
        Member memberD =Member.builder()
                .name("memberD")
                .email("memberD@kookmin.ac.kr")
                .nickname("memberNicknameD")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(memberD);

        Meeting meetingB = Meeting.builder()
                .category(category)
                .member(memberD)
                .title("Meeting")
                .text("meet")
                .place("D")
                .meetDate(LocalDateTime.of(2021,06,05,00,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                .dDay(2L)
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meetingB);


        //when
        //meetingA dDay=0, meeting dDay=1
        noticeService.dDayNotice();

        //then
        List<Notice> memberAList = noticeRepository.getNoticeList(meetingA.getMember().getId());
        List<Notice> memberBList = noticeRepository.getNoticeList(memberB.getId());
        List<Notice> memberCList = noticeRepository.getNoticeList(memberC.getId());

        for (Notice notice : memberAList){
            System.out.println(notice.toString());
        }

        Assertions.assertThat(memberAList.size()).isEqualTo(1);
        Assertions.assertThat(memberBList.size()).isEqualTo(1);
        Assertions.assertThat(memberCList.size()).isEqualTo(1);

        //meetingB 는 알림 생성x
        List<Notice> memberDList = noticeRepository.getNoticeList(meetingB.getMember().getId());
        Assertions.assertThat(memberDList.size()).isEqualTo(0);
    }


    @Test
    void 알림_리스트() throws Exception{
        //given
        Notice notice1 = createNotice();
        Notice notice2 = createNotice();
        Notice notice3 = createNotice();
        Notice notice4 = createNotice();
        Notice notice5 = createNotice();
        Notice notice6 = createNotice();
        Notice notice7 = createNotice();
        Notice notice8 = createNotice();
        Notice notice9 = createNotice();
        Notice notice10 = createNotice();
        Notice notice11 = createNotice();


        PageRequestDTO requestDTO = new PageRequestDTO();

        //when
        PageResultDTO<NoticeDTO, Notice> result = noticeService.getNoticeList(requestDTO, 1L);

        //then
        Assertions.assertThat(result.getTotalPage()).isEqualTo(2);
        Assertions.assertThat(result.getSize()).isEqualTo(10);
        Assertions.assertThat(result.getStart()).isEqualTo(1);
        Assertions.assertThat(result.getDtoList().size()).isEqualTo(10);
    }

    @Test
    void 읽음표시() throws Exception{
        //given
        Notice notice = createNotice();

        //when
        noticeService.changeRead(notice.getId());

        //then
        Optional<Notice> result = noticeRepository.findById(notice.getId());
        Notice notice1 = result.get();
        Assertions.assertThat(notice1.getState()).isTrue();
    }

    @Test
    void 안읽음_개수() throws Exception{
        //given
        Notice notice1 = createNotice();
        Notice notice2 = createNotice();
        Notice notice3 = createNotice();

        notice3.changeState();

        //when
        Assertions.assertThat(noticeRepository.getUnreadCount(1L)).isEqualTo(2);

    }

    private Notice createNotice() {

        Notice notice = Notice.builder()
                .memberId(1L)
                .text("형우님이 팔로우 신청을 했습니다.")
                .link("/member/guddn")
                .state(false)
                .build();

        noticeRepository.save(notice);

        return notice;

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
                .dDay(1L)
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meeting);

        return meeting;
    }

    private Member createMember() {
        Member member = Member.builder()
                .name("memberA")
                .email("memberA@kookmin.ac.kr")
                .nickname("memberNicknameA")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(member);

        return member;
    }

    private Category createCategory() {
        Category category = Category.builder()
                .name("categoryA")
                .build();

        categoryRepository.save(category);

        return category;
    }
}