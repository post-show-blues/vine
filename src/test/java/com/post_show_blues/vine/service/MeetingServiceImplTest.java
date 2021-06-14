package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.meetingimg.MeetingImgRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.dto.MeetingDTO;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class MeetingServiceImplTest {

    @Autowired MeetingService meetingService;
    @Autowired MeetingRepository meetingRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ParticipantRepository participantRepository;
    @Autowired MeetingImgRepository meetingImgRepository;

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
    void 등록시_meetDate_deadline_비교() throws Exception{
        //given
        Member member = createMember();
        Category category = createCategory();

        MeetingDTO meetingDTO = MeetingDTO.builder()
                .categoryId(category.getId())
                .masterId(member.getId())
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate("2021-06-03")
                .reqDeadline("2021-06-04")
                .maxNumber(4)
                .build();
        
        //when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> meetingService.register(meetingDTO));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("활동일이 신청마감일보다 빠릅니다.");
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

    @Test
    void 인원수정오류() throws Exception{
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
                .maxNumber(2) // 4 -> 2로 변경
                .build();

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> meetingService.modify(meetingDTO));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("참여인원 초과입니다.");

    }
    
    /*@Test
    void 모임삭제() throws Exception{
        //given
        Participant participant = createParticipant();
        Long meetingId = participant.getMeeting().getId();

        //when
        meetingService.remove(meetingId);

        //then

        System.out.println(participantRepository.findById(participant.getId()));

        //삭제된 모임방 검색
        NoSuchElementException e2 = assertThrows(NoSuchElementException.class,
                () -> meetingService.findOne(meetingId));

        NoSuchElementException e1 = assertThrows(NoSuchElementException.class,
                () -> (participantRepository.findById(participant.getId())).get());


        Assertions.assertThat(e1.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e2.getMessage()).isEqualTo("No value present");

    }*/

    @Test
    void 모임_조회페이지DTO() throws Exception{
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
        MeetingDTO meetingDTO = meetingService.getMeeting(meeting.getId());

        //then
        Assertions.assertThat(meetingDTO.getMeetingId()).isEqualTo(meeting.getId());
        Assertions.assertThat(meetingDTO.getCategoryName()).isEqualTo("categoryA");
        Assertions.assertThat(meetingDTO.getImgDTOList().size()).isEqualTo(2);
    }

    @Test
    void 모임_조회페이지DTO_사진x경우() throws Exception{
        //given
        Meeting meeting = createMeeting();

        //when
        MeetingDTO meetingDTO = meetingService.getMeeting(meeting.getId());

        //then
        Assertions.assertThat(meetingDTO.getMeetingId()).isEqualTo(meeting.getId());
        Assertions.assertThat(meetingDTO.getCategoryName()).isEqualTo("categoryA");
        Assertions.assertThat(meetingDTO.getImgDTOList().size()).isEqualTo(0);
    }

    private Participant createParticipant() {
        Meeting meeting = createMeeting();
        Member member = createMember();

        Participant participant = Participant.builder()
                .meeting(meeting)
                .member(member)
                .req(false)
                .build();

        participantRepository.save(participant);

        return participant;
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