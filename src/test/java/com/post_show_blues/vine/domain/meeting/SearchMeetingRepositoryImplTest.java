package com.post_show_blues.vine.domain.meeting;

import com.post_show_blues.vine.domain.bookmark.Bookmark;
import com.post_show_blues.vine.domain.bookmark.BookmarkRepository;
import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.follow.Follow;
import com.post_show_blues.vine.domain.follow.FollowRepository;
import com.post_show_blues.vine.domain.heart.HeartRepository;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.meetingimg.MeetingImgRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
class SearchMeetingRepositoryImplTest {

    @Autowired MeetingRepository meetingRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberImgRepository memberImgRepository;
    @Autowired ParticipantRepository participantRepository;
    @Autowired FollowRepository followRepository;
    @Autowired MeetingImgRepository meetingImgRepository;
    @Autowired BookmarkRepository bookmarkRepository;
    @Autowired HeartRepository heartRepository;

    @Test
    void 검색리스트() throws Exception{
        //given
        //meeting 생성
        IntStream.rangeClosed(1,5).forEach(i -> {

            Member member = Member.builder()
                    .email("member"+i+"@kookmin.ac.kr")
                    .nickname("member"+i+"Nickname")
                    .password("1111")
                    .build();
            memberRepository.save(member);

            MemberImg memberImg = MemberImg.builder()
                    .member(member)
                    .folderPath("vine/2021/09/21")
                    .storeFileName(i+"231f@Rfl_file1.jpeg")
                    .build();
            memberImgRepository.save(memberImg);

            Meeting meeting = Meeting.builder()
                    .category(Category.SPORTS)
                    .member(member)
                    .title("Meeting"+i)
                    .text("meet")
                    .place("A")
                    .meetDate(LocalDateTime.of(2023,8,06,00,00))
                    .reqDeadline(LocalDateTime.of(2023,06,04,00,00))
                    .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                            LocalDateTime.of(2023,8,05,00,00)
                                    .toLocalDate().atStartOfDay()).toDays())
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

        meeting1.changeCategory(Category.MUSIC); // category = "음악"
        meeting2.changeCategory(Category.SPORTS); // category = "스포츠"
        meeting3.changeCategory(Category.SPORTS); // category = "스포츠"
        meeting4.changeCategory(Category.DANCE); // category = "춤"
        meeting5.changeCategory(Category.TRAVEL); // category = "여행"

        meeting2.changeTitle("10시까지 풋살 모집"); // meeting2
        meeting4.changeTitle("풋살할 사람"); //meeting4

        //모임 사진 등록 meeting2 -> 사진 2개
        MeetingImg meetingImgA = MeetingImg.builder()
                .meeting(meeting2)
                .folderPath("vine/2021/09/21")
                .storeFileName("231f@Rfl_사진1.jpeg")
                .build();
        meetingImgRepository.save(meetingImgA);

        MeetingImg meetingImgB = MeetingImg.builder()
                .meeting(meeting2)
                .folderPath("vine/2021/09/21")
                .storeFileName("231f@Rfl_사진2.jpeg")
                .build();
        meetingImgRepository.save(meetingImgB);

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .categoryList(List.of(Category.SPORTS, Category.DANCE))
                .keyword("풋살")
                .page(1)
                .size(10)
                .build();

        //when
        /**
         * meeting2,3 (스포츠), meeting4 (춤)
         * meeting2,4 title =  ~풋살~
         * = meeting2,4만 출력
         * meeting2 사진 2개 등록 -> id 제일 작은 사진 1개 출력
         * meeting4 사진 x
         */
        Page<Object[]> result = meetingRepository.searchPage(pageRequestDTO.getCategoryList(), pageRequestDTO.getKeyword(),
                null, pageRequestDTO.getPageable(Sort.by("id").descending()));

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

            Member member = Member.builder()
                    .email("member"+i+"@kookmin.ac.kr")
                    .nickname("member"+i+"Nickname")
                    .password("1111")
                    .build();
            memberRepository.save(member);

            MemberImg memberImg = MemberImg.builder()
                    .member(member)
                    .folderPath("vine/2021/09/21")
                    .storeFileName(i+"231f@Rfl_file1.jpeg")
                    .build();
            memberImgRepository.save(memberImg);

            Meeting meeting = Meeting.builder()
                    .category(Category.SPORTS)
                    .member(member)
                    .title("Meeting"+i)
                    .text("meet")
                    .place("A")
                    .meetDate(LocalDateTime.of(2023,8,06,00,00))
                    .reqDeadline(LocalDateTime.of(2023,06,04,00,00))
                    .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                            LocalDateTime.of(2023,8,05,00,00)
                                    .toLocalDate().atStartOfDay()).toDays())
                    .maxNumber(4)
                    .currentNumber(3)
                    .build();
            meetingRepository.save(meeting);

        });

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(3)
                .build();

        //when
        Page<Object[]> result = meetingRepository.searchPage(pageRequestDTO.getCategoryList(),
                pageRequestDTO.getKeyword(), null,
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

            Member member = Member.builder()
                    .email("member"+i+"@kookmin.ac.kr")
                    .nickname("member"+i+"Nickname")
                    .password("1111")
                    .build();
            memberRepository.save(member);

            MemberImg memberImg = MemberImg.builder()
                    .member(member)
                    .folderPath("vine/2021/09/21")
                    .storeFileName(i+"231f@Rfl_file1.jpeg")
                    .build();
            memberImgRepository.save(memberImg);

            Meeting meeting = Meeting.builder()
                    .category(Category.SPORTS)
                    .member(member)
                    .title("Meeting"+i)
                    .text("meet")
                    .place("A")
                    .meetDate(LocalDateTime.of(2023,8,06,00,00))
                    .reqDeadline(LocalDateTime.of(2023,06,04,00,00))
                    .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                            LocalDateTime.of(2023,8,05,00,00)
                                    .toLocalDate().atStartOfDay()).toDays())
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
        Page<Object[]> result = meetingRepository.searchPage(pageRequestDTO.getCategoryList(), pageRequestDTO.getKeyword(),
                null, pageRequestDTO.getPageable(Sort.by("id").descending()));

        //then
        for (Object[] arr : result.getContent()){
            System.out.println(Arrays.toString(arr));
        }

        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);

    }

    @Test
    void 리스트조회_카테고리검색() throws Exception{
        //given
        //meeting 생성
        IntStream.rangeClosed(1,5).forEach(i -> {

            Member member = Member.builder()
                    .email("member"+i+"@kookmin.ac.kr")
                    .nickname("member"+i+"Nickname")
                    .password("1111")
                    .build();
            memberRepository.save(member);

            MemberImg memberImg = MemberImg.builder()
                    .member(member)
                    .folderPath("vine/2021/09/21")
                    .storeFileName(i+"231f@Rfl_file1.jpeg")
                    .build();
            memberImgRepository.save(memberImg);

            Meeting meeting = Meeting.builder()
                    .category(Category.SPORTS)
                    .member(member)
                    .title("Meeting"+i)
                    .text("meet")
                    .place("A")
                    .meetDate(LocalDateTime.of(2023,8,06,00,00))
                    .reqDeadline(LocalDateTime.of(2023,06,04,00,00))
                    .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                            LocalDateTime.of(2023,8,05,00,00)
                                    .toLocalDate().atStartOfDay()).toDays())
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

        meeting1.changeCategory(Category.MUSIC); // category = "음악"
        meeting2.changeCategory(Category.SPORTS); // category = "스포츠"
        meeting3.changeCategory(Category.SPORTS); // category = "스포츠"
        meeting4.changeCategory(Category.DANCE); // category = "춤"
        meeting5.changeCategory(Category.TRAVEL); // category = "여행"

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .categoryList(List.of(Category.SPORTS,Category.DANCE))
                .page(1)
                .size(10)
                .build();

        //when
        // meeting2,3,4 만 카테고리 검색에 해당 (스포츠, 춤)
        Page<Object[]> result = meetingRepository.searchPage(pageRequestDTO.getCategoryList(), pageRequestDTO.getKeyword(),
                null, pageRequestDTO.getPageable(Sort.by("id").descending()));

        //then
        for (Object[] arr : result.getContent()){
            System.out.println(Arrays.toString(arr));
        }

        Assertions.assertThat(result.getTotalElements()).isEqualTo(3);
    }

    @Test
    void 리스트조회_팔로우() throws Exception{
        //given
        //meeting 생성
        IntStream.rangeClosed(1,5).forEach(i -> {

            Member member = Member.builder()
                    .email("member"+i+"@kookmin.ac.kr")
                    .nickname("member"+i+"Nickname")
                    .password("1111")
                    .build();
            memberRepository.save(member);

            MemberImg memberImg = MemberImg.builder()
                    .member(member)
                    .folderPath("vine/2021/09/21")
                    .storeFileName(i+"231f@Rfl_file1.jpeg")
                    .build();
            memberImgRepository.save(memberImg);

            Meeting meeting = Meeting.builder()
                    .category(Category.SPORTS)
                    .member(member)
                    .title("Meeting"+i)
                    .text("meet")
                    .place("A")
                    .meetDate(LocalDateTime.of(2023,8,06,00,00))
                    .reqDeadline(LocalDateTime.of(2023,06,04,00,00))
                    .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                            LocalDateTime.of(2023,8,05,00,00)
                                    .toLocalDate().atStartOfDay()).toDays())
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

        meeting1.changeCategory(Category.MUSIC); // category = "음악"
        meeting2.changeCategory(Category.SPORTS); // category = "스포츠"
        meeting3.changeCategory(Category.SPORTS); // category = "스포츠"
        meeting4.changeCategory(Category.DANCE); // category = "춤"
        meeting5.changeCategory(Category.TRAVEL); // category = "여행"

        meeting2.changeTitle("10시까지 풋살 모집"); // meeting2
        meeting4.changeTitle("풋살할 사람"); //meeting4

        //팔로우 - 팔로우하지 않은 사람(자기자신)의 모임은 출력되면 안됨. = 팔로우한 사람이 방장인 모임만 출력되어야 함.
        Member memberUser = Member.builder()
                .email("memberUser@kookmin.ac.kr")
                .nickname("memberUserNickname")
                .password("1111")
                .build();
        memberRepository.save(memberUser);

        followRepository.rFollow(memberUser.getId(), meeting1.getMember().getId());
        followRepository.rFollow(memberUser.getId(), meeting2.getMember().getId());
        followRepository.rFollow(memberUser.getId(), meeting3.getMember().getId());
        followRepository.rFollow(memberUser.getId(), meeting4.getMember().getId());
        followRepository.rFollow(memberUser.getId(), meeting5.getMember().getId());

        //본인이 만든 모임 - 출력되면 x (키워드 "풋살" 포함, 카테코리 "SPORTS")
        Meeting meetingUser = Meeting.builder()
                .category(Category.SPORTS)
                .member(memberUser)
                .title("풋살이지만 출력되면 안돼")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2023,8,06,00,00))
                .reqDeadline(LocalDateTime.of(2023,06,04,00,00))
                .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                        LocalDateTime.of(2023,8,05,00,00)
                                .toLocalDate().atStartOfDay()).toDays())
                .maxNumber(4)
                .currentNumber(3)
                .build();
        meetingRepository.save(meetingUser);


        //모임 사진 등록 meeting2 -> 사진 2개
        MeetingImg meetingImgA = MeetingImg.builder()
                .meeting(meeting2)
                .folderPath("vine/2021/09/21")
                .storeFileName("231f@Rfl_사진1.jpeg")
                .build();
        meetingImgRepository.save(meetingImgA);

        MeetingImg meetingImgB = MeetingImg.builder()
                .meeting(meeting2)
                .folderPath("vine/2021/09/21")
                .storeFileName("231f@Rfl_사진2.jpeg")
                .build();
        meetingImgRepository.save(meetingImgB);

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .categoryList(List.of(Category.SPORTS, Category.DANCE))
                .keyword("풋살")
                .page(1)
                .size(10)
                .build();

        //when
        /**
         * meeting1,2,3,4,5 는 팔로우 / meetingUser 은 자기자신이 만든 모임 (팔로우x)
         * meeting2,3 (스포츠), meeting4 (춤)
         * meeting2,4 title =  ~풋살~
         * = meeting2,4만 출력
         * meeting2 모임 사진 2개 -> id 제일 작인거 1개만 출력
         * meeting4 모임 사진 x
         */
        Page<Object[]> result = meetingRepository.searchPage(pageRequestDTO.getCategoryList(), pageRequestDTO.getKeyword(),
                memberUser.getId(), pageRequestDTO.getPageable(Sort.by("id").descending()));

        //then
        for (Object[] arr : result.getContent()){
            System.out.println(Arrays.toString(arr));
        }

        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 북마크_리스트() throws Exception{
        //given
        //meeting 생성
        IntStream.rangeClosed(1,5).forEach(i -> {

            Member member = Member.builder()
                    .email("member"+i+"@kookmin.ac.kr")
                    .nickname("member"+i+"Nickname")
                    .password("1111")
                    .build();
            memberRepository.save(member);

            MemberImg memberImg = MemberImg.builder()
                    .member(member)
                    .folderPath("vine/2021/09/21")
                    .storeFileName(i+"231f@Rfl_file1.jpeg")
                    .build();
            memberImgRepository.save(memberImg);

            Meeting meeting = Meeting.builder()
                    .category(Category.SPORTS)
                    .member(member)
                    .title("Meeting"+i)
                    .text("meet")
                    .place("A")
                    .meetDate(LocalDateTime.of(2023,8,06,00,00))
                    .reqDeadline(LocalDateTime.of(2023,06,04,00,00))
                    .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                            LocalDateTime.of(2023,8,05,00,00)
                                    .toLocalDate().atStartOfDay()).toDays())
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

        //모임 사진 등록 meeting4 -> 사진 2개
        MeetingImg meetingImgA = MeetingImg.builder()
                .meeting(meeting4)
                .folderPath("vine/2021/09/21")
                .storeFileName("231f@Rfl_사진1.jpeg")
                .build();
        meetingImgRepository.save(meetingImgA);

        MeetingImg meetingImgB = MeetingImg.builder()
                .meeting(meeting4)
                .folderPath("vine/2021/09/21")
                .storeFileName("231f@Rfl_사진2.jpeg")
                .build();
        meetingImgRepository.save(meetingImgB);

        //현재 사용 유저 생성
        Member memberUser = Member.builder()
                .email("memberUser@kookmin.ac.kr")
                .nickname("memberUserNickname")
                .password("1111")
                .build();
        memberRepository.save(memberUser);

        //memberUser 가 meeting1,4 를 북마크 등록
        Bookmark bookmarkA = Bookmark.builder()
                .meeting(meeting1)
                .member(memberUser)
                .build();

        bookmarkRepository.save(bookmarkA);

        Bookmark bookmarkB = Bookmark.builder()
                .meeting(meeting4)
                .member(memberUser)
                .build();

        bookmarkRepository.save(bookmarkB);

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        //when
        Page<Object[]> result = meetingRepository.bookmarkPage(
                memberUser.getId(), pageRequestDTO.getPageable(Sort.by("id").descending()));

        /**
         *
         */
        //then
        for (Object[] arr : result.getContent()){
            System.out.println(Arrays.toString(arr));
        }

        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
    }



}