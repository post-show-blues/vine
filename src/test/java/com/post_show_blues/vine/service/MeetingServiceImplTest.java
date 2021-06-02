package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.dto.MeetingDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MeetingServiceImplTest {

    @Autowired MeetingService meetingService;
    @Autowired MeetingRepository meetingRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired MemberRepository memberRepository;

   @Test
   //TODO 2021.06.02 - 사진등록 테스트는? -hyeongwoo
    void 모임등록() throws Exception{
        //given

       Member member = createMember();
       Category category = createCategory();

       MeetingDTO meetingDTO = MeetingDTO.builder()
                .categoryId(category.getId())
                .masterId(member.getId())
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate("2021-06-05")
                .reqDeadline("2021-06-04")
                .maxNumber(4)
                .build();

        //when
        Long saveId = meetingService.register(meetingDTO);

        //then
        Meeting meeting = meetingService.findOne(saveId);
        Assertions.assertThat("MeetingA").isEqualTo(meeting.getTitle());
        Assertions.assertThat(member.getId()).isEqualTo(meeting.getMember().getId());
        Assertions.assertThat(category.getId()).isEqualTo(meeting.getCategory().getId());

    }

    @Test
    //TODO 2021.06.02 - 사진변경 테스트는? -hyeongwoo
    void 모임수정() throws Exception{
        //given
        Meeting meeting = createMeeting();

        Category category1 = createCategory();
        Member member1 = createMember();

        MeetingDTO meetingDTO = MeetingDTO.builder()
                .meetingId(meeting.getId())
                .categoryId(category1.getId())
                .masterId(member1.getId())
                .title("MeetingB") //meetingA -> meeting B로 변경
                .text("meet2") //meet -> meet2
                .place("B") // A -> B
                .meetDate("2021-06-05")
                .reqDeadline("2021-06-04")
                .maxNumber(5) // 4 -> 5로 변경
                .build();

        //when
        meetingService.modify(meetingDTO);

        //then
        Meeting result = meetingService.findOne(meeting.getId());

        Assertions.assertThat(result.getTitle()).isEqualTo("MeetingB");
        Assertions.assertThat(result.getMember().getId()).isEqualTo(member1.getId());
        Assertions.assertThat(result.getCategory().getId()).isEqualTo(category1.getId());

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