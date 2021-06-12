package com.post_show_blues.vine.domain.meeting;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.category.CategoryRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.dto.PageRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SearchMeetingRepositoryImplTest {

    @Autowired MeetingRepository meetingRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberImgRepository memberImgRepository;
    @Autowired ParticipantRepository participantRepository;

    @Test
    void 검색리스트() throws Exception{
        //given

        //카테고리 생성
        Category category1 = Category.builder().name("스포츠").build();
        Category category2 = Category.builder().name("음악").build();
        Category category3 = Category.builder().name("맛집").build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        //meeting 생성
        Meeting meeting1 = createMeeting();
        Meeting meeting2 = createMeeting();
        Meeting meeting3 = createMeeting();
        Meeting meeting4 = createMeeting();
        Meeting meeting5 = createMeeting();

        meeting1.changeCategory(category2); // category = "음악"
        meeting2.changeCategory(category1); // category = "스포츠"
        meeting3.changeCategory(category1); // category = "스포츠"
        meeting4.changeCategory(category1); // category = "스포츠"
        meeting5.changeCategory(category3); // category = "맛집"

        meeting2.changeTitle("10시까지 풋살 모집"); // meeting2
        meeting4.changeTitle("풋살할 사람"); //meeting4

        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);
        meetingRepository.save(meeting3);
        meetingRepository.save(meeting4);
        meetingRepository.save(meeting5);

        //participant 생성
        MemberImg memberImg1 = createMemberImg();
        Member member1 = memberImg1.getMember();
        Participant participant1 = Participant.builder().meeting(meeting2).member(member1).req(false).build();

        MemberImg memberImg2 = createMemberImg();
        Member member2 = memberImg2.getMember();
        Participant participant2 = Participant.builder().meeting(meeting2).member(member2).req(true).build();

        MemberImg memberImg3 = createMemberImg();
        Member member3 = memberImg3.getMember();
        Participant participant3 = Participant.builder().meeting(meeting2).member(member3).req(true).build();

        participantRepository.save(participant1);
        participantRepository.save(participant2);
        participantRepository.save(participant3);

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .category(category1)
                .keyword("풋살")
                .page(1)
                .size(10)
                .build();

        //when
        Page<Object[]> result = meetingRepository.searchPage(pageRequestDTO.getCategory(), pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("id").descending()));

        //then
        for (Object[] arr : result.getContent()){
            System.out.println(Arrays.toString(arr));
        }
    }

    private Meeting createMeeting() {
        Category category = createCategory();
        MemberImg memberImg = createMemberImg();
        Member member = memberImg.getMember();

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


}