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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

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
        IntStream.rangeClosed(1,5).forEach(i -> {

            Category category = createCategory();

            Member member = Member.builder()
                    .name("member"+i)
                    .email("member"+i+"@kookmin.ac.kr")
                    .nickname("member"+i+"Nickname")
                    .password("1111")
                    .phone("010-0000-0000")
                    .university("국민대학교")
                    .build();
            memberRepository.save(member);

            MemberImg memberImg = MemberImg.builder()
                    .member(member)
                    .fileName("MemberImg"+i)
                    .filePath("/hyeongwoo")
                    .uuid(UUID.randomUUID().toString())
                    .build();
            memberImgRepository.save(memberImg);

            Meeting meeting = Meeting.builder()
                    .category(category)
                    .member(member)
                    .title("Meeting"+i)
                    .text("meet")
                    .place("A")
                    .meetDate(LocalDateTime.of(2021,8,06,00,00))
                    .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                    .dDay(Period.between(LocalDate.now(),
                            LocalDateTime.of(2021,8,05,00,00)
                                    .toLocalDate()).getDays())
                    .maxNumber(4)
                    .currentNumber(3)
                    .build();
            meetingRepository.save(meeting);

        });

        List<Meeting> meetingList = meetingRepository.findAll();

        Meeting meeting1 = meetingList.get(0);
        Meeting meeting2 = meetingList.get(1);
        Meeting meeting3 = meetingList.get(2);
        Meeting meeting4 = meetingList.get(3);
        Meeting meeting5 = meetingList.get(4);

        meeting1.changeCategory(category2); // category = "음악"
        meeting2.changeCategory(category1); // category = "스포츠"
        meeting3.changeCategory(category1); // category = "스포츠"
        meeting4.changeCategory(category1); // category = "스포츠"
        meeting5.changeCategory(category3); // category = "맛집"

        meeting2.changeTitle("10시까지 풋살 모집"); // meeting2
        meeting4.changeTitle("풋살할 사람"); //meeting4

        //participant 생성
        IntStream.rangeClosed(6,8).forEach(i->{
            Member member = Member.builder()
                    .name("member"+i)
                    .email("member"+i+"@kookmin.ac.kr")
                    .nickname("member"+i+"Nickname")
                    .password("1111")
                    .phone("010-0000-0000")
                    .university("국민대학교")
                    .build();
            memberRepository.save(member);

            MemberImg memberImg = MemberImg.builder()
                    .member(member)
                    .fileName("MemberImg"+i)
                    .filePath("/hyeongwoo")
                    .uuid(UUID.randomUUID().toString())
                    .build();
            memberImgRepository.save(memberImg);

            Participant participant = Participant.builder()
                    .meeting(meeting2)
                    .member(member)
                    .build();

            participantRepository.save(participant);
        });

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .category(category1)
                .keyword("풋살")
                .page(1)
                .size(10)
                .build();

        //when
        /**
         * meeting2,3,4 category1(스포츠)
         * meeting2,4 title =  ~풋살~
         * = meeting2,4만 출력
         * meeting2 참여자 3
         * meeting4 참여자 x
         */
        Page<Object[]> result = meetingRepository.searchPage(pageRequestDTO.getCategory(), pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("id").descending()));

        //then
        for (Object[] arr : result.getContent()){
            System.out.println(Arrays.toString(arr));
        }

        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 리스트조회_검색x() throws Exception{
        //given
        //meeting 생성
        IntStream.rangeClosed(1,5).forEach(i -> {

            Category category = createCategory();

            Member member = Member.builder()
                    .name("member"+i)
                    .email("member"+i+"@kookmin.ac.kr")
                    .nickname("member"+i+"Nickname")
                    .password("1111")
                    .phone("010-0000-0000")
                    .university("국민대학교")
                    .build();
            memberRepository.save(member);

            MemberImg memberImg = MemberImg.builder()
                    .member(member)
                    .fileName("MemberImg"+i)
                    .filePath("/hyeongwoo")
                    .uuid(UUID.randomUUID().toString())
                    .build();
            memberImgRepository.save(memberImg);

            Meeting meeting = Meeting.builder()
                    .category(category)
                    .member(member)
                    .title("Meeting"+i)
                    .text("meet")
                    .place("A")
                    .meetDate(LocalDateTime.of(2021,8,06,00,00))
                    .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                    .dDay(Period.between(LocalDate.now(),
                            LocalDateTime.of(2021,8,05,00,00)
                                    .toLocalDate()).getDays())
                    .maxNumber(4)
                    .currentNumber(3)
                    .build();
            meetingRepository.save(meeting);

        });

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(3).build();

        //when
        Page<Object[]> result = meetingRepository.searchPage(pageRequestDTO.getCategory(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("id").descending()));

        //then
        for (Object[] arr : result.getContent()){
            System.out.println(Arrays.toString(arr));
        }

        Assertions.assertThat(result.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(result.getSize()).isEqualTo(3);
    }

    @Test
    void 리스트조회_키워드검색() throws Exception{
        //given
        //meeting 생성
        IntStream.rangeClosed(1,5).forEach(i -> {

            Category category = createCategory();

            Member member = Member.builder()
                    .name("member"+i)
                    .email("member"+i+"@kookmin.ac.kr")
                    .nickname("member"+i+"Nickname")
                    .password("1111")
                    .phone("010-0000-0000")
                    .university("국민대학교")
                    .build();
            memberRepository.save(member);

            MemberImg memberImg = MemberImg.builder()
                    .member(member)
                    .fileName("MemberImg"+i)
                    .filePath("/hyeongwoo")
                    .uuid(UUID.randomUUID().toString())
                    .build();
            memberImgRepository.save(memberImg);

            Meeting meeting = Meeting.builder()
                    .category(category)
                    .member(member)
                    .title("Meeting"+i)
                    .text("meet")
                    .place("A")
                    .meetDate(LocalDateTime.of(2021,8,06,00,00))
                    .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                    .dDay(Period.between(LocalDate.now(),
                            LocalDateTime.of(2021,8,05,00,00)
                                    .toLocalDate()).getDays())
                    .maxNumber(4)
                    .currentNumber(3)
                    .build();
            meetingRepository.save(meeting);
        });

        List<Meeting> meetingList = meetingRepository.findAll();
        Meeting meeting2 = meetingList.get(1);
        Meeting meeting4 = meetingList.get(3);

        meeting2.changeTitle("10시까지 풋살 모집"); // meeting2
        meeting4.changeTitle("풋살할 사람"); //meeting4

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .keyword("풋살")
                .page(1)
                .size(10)
                .build();

        //when
        // meeting 2,4 만 title 풋살 포함
        Page<Object[]> result = meetingRepository.searchPage(pageRequestDTO.getCategory(), pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("id").descending()));

        //then
        for (Object[] arr : result.getContent()){
            System.out.println(Arrays.toString(arr));
        }

        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);

    }

    @Test
    void 리스트조회_카테고리검색() throws Exception{
        //given
        //카테고리 생성
        Category category1 = Category.builder().name("스포츠").build();
        Category category2 = Category.builder().name("음악").build();
        Category category3 = Category.builder().name("맛집").build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        //meeting 생성
        IntStream.rangeClosed(1,5).forEach(i -> {

            Category category = createCategory();

            Member member = Member.builder()
                    .name("member"+i)
                    .email("member"+i+"@kookmin.ac.kr")
                    .nickname("member"+i+"Nickname")
                    .password("1111")
                    .phone("010-0000-0000")
                    .university("국민대학교")
                    .build();
            memberRepository.save(member);

            MemberImg memberImg = MemberImg.builder()
                    .member(member)
                    .fileName("MemberImg"+i)
                    .filePath("/hyeongwoo")
                    .uuid(UUID.randomUUID().toString())
                    .build();
            memberImgRepository.save(memberImg);

            Meeting meeting = Meeting.builder()
                    .category(category)
                    .member(member)
                    .title("Meeting"+i)
                    .text("meet")
                    .place("A")
                    .meetDate(LocalDateTime.of(2021,8,06,00,00))
                    .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                    .dDay(Period.between(LocalDate.now(),
                            LocalDateTime.of(2021,8,05,00,00)
                                    .toLocalDate()).getDays())
                    .maxNumber(4)
                    .currentNumber(3)
                    .build();
            meetingRepository.save(meeting);

        });

        List<Meeting> meetingList = meetingRepository.findAll();

        Meeting meeting1 = meetingList.get(0);
        Meeting meeting2 = meetingList.get(1);
        Meeting meeting3 = meetingList.get(2);
        Meeting meeting4 = meetingList.get(3);
        Meeting meeting5 = meetingList.get(4);

        meeting1.changeCategory(category2); // category = "음악"
        meeting2.changeCategory(category1); // category = "스포츠"
        meeting3.changeCategory(category1); // category = "스포츠"
        meeting4.changeCategory(category1); // category = "스포츠"
        meeting5.changeCategory(category3); // category = "맛집"

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .category(category1)
                .page(1)
                .size(10)
                .build();

        //when
        // meeting2,3,4 만 category1
        Page<Object[]> result = meetingRepository.searchPage(pageRequestDTO.getCategory(), pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("id").descending()));

        //then
        for (Object[] arr : result.getContent()){
            System.out.println(Arrays.toString(arr));
        }

        Assertions.assertThat(result.getTotalElements()).isEqualTo(3);
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
                .meetDate(LocalDateTime.of(2021,06,05,00,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
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