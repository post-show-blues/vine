package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.bookmark.Bookmark;
import com.post_show_blues.vine.domain.bookmark.BookmarkRepository;
import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.domain.comment.CommentRepository;
import com.post_show_blues.vine.domain.follow.FollowRepository;
import com.post_show_blues.vine.domain.heart.Heart;
import com.post_show_blues.vine.domain.heart.HeartRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.meetingimg.MeetingImgRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.domain.requestParticipant.RequestParticipant;
import com.post_show_blues.vine.domain.requestParticipant.RequestParticipantRepository;
import com.post_show_blues.vine.dto.member.MemberImgDTO;
import com.post_show_blues.vine.dto.meeting.DetailMeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingResDTO;
import com.post_show_blues.vine.dto.member.MemberListDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.dto.participant.ParticipantDTO;
import com.post_show_blues.vine.handler.exception.CustomException;
import com.post_show_blues.vine.service.meeting.MeetingService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class MeetingServiceImplTest {

    @Autowired MeetingService meetingService;
    @Autowired MeetingRepository meetingRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ParticipantRepository participantRepository;
    @Autowired MeetingImgRepository meetingImgRepository;
    @Autowired RequestParticipantRepository requestParticipantRepository;
    @Autowired MemberImgRepository memberImgRepository;
    @Autowired FollowRepository followRepository;
    @Autowired NoticeRepository noticeRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired BookmarkRepository bookmarkRepository;
    @Autowired HeartRepository heartRepository;


   @Test
    void ????????????() throws Exception{
       //given
       Member memberA = createMember();

       //memberA??? ????????? ?????? memberB,C ??????
       Member memberB = Member.builder()
               .email("memberB@kookmin.ac.kr")
               .nickname("memberNicknameB")
               .password("1111")
               .build();

       memberRepository.save(memberB);

       Member memberC = Member.builder()
               .email("memberC@kookmin.ac.kr")
               .nickname("memberNicknameC")
               .password("1111")
               .build();

       memberRepository.save(memberC);

       //memberB,C ??? A??? ????????? -> A??? ??????????????? B,C?????? ?????? ??????
       followRepository.rFollow(memberB.getId(), memberA.getId());
       followRepository.rFollow(memberC.getId(), memberA.getId());

       //mock ????????? ?????? meeting.imgFiles ??????
       List<MultipartFile> imgFiles = new ArrayList<>();

       MultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());
       MultipartFile file2 = new MockMultipartFile("file", "filename-2.jpeg", "image/jpeg", "some-image".getBytes());

       imgFiles.add(file1);
       imgFiles.add(file2);


       //meetingDTO ??????
       MeetingDTO meetingDTO = MeetingDTO.builder()
               .category(Category.SPORTS)
               .title("MeetingA")
               .text("meet")
               .place("A")
               .meetDate(LocalDateTime.of(2021,10,13,16,00))
               .reqDeadline(LocalDateTime.of(2021,10,10,16,00))
               .maxNumber(4)
               .imageFiles(imgFiles) //????????? ??????
               .build();

       //when
       Long saveId = meetingService.register(meetingDTO, memberA.getId());

       //then
       //?????? ??????
       Meeting meeting = meetingService.findOne(saveId);
       Assertions.assertThat(meeting.getTitle()).isEqualTo("MeetingA");
       Assertions.assertThat(meeting.getMember().getId()).isEqualTo(memberA.getId());
       Assertions.assertThat(meeting.getCategory()).isEqualTo(meetingDTO.getCategory());
       Assertions.assertThat(meeting.getCurrentNumber()).isEqualTo(1);
       //Assertions.assertThat(meeting.getDDay()).isEqualTo(5);

       //?????? imgFiles ??????
       List<MeetingImg> meetingImgList = meetingImgRepository.findByMeeting(meeting);
       Assertions.assertThat(meetingImgList.size()).isEqualTo(2);

       //????????? ????????? ??????
       List<Participant> participantList = participantRepository.findByMeeting(meeting);
       Participant participant = participantList.get(0);

       Assertions.assertThat(participantList.size()).isEqualTo(1);
       Assertions.assertThat(participant.getId()).isNotNull();
       Assertions.assertThat(participant.getMeeting().getId()).isEqualTo(meeting.getId());
       Assertions.assertThat(participant.getMember().getId()).isEqualTo(memberA.getId());


       //?????? ??????
       List<Notice> noticeListB = noticeRepository.getNoticeList(memberB.getId());
       List<Notice> noticeListC = noticeRepository.getNoticeList(memberC.getId());

       Assertions.assertThat(noticeListB.size()).isEqualTo(1);
       Assertions.assertThat(noticeListC.size()).isEqualTo(1);

       for (Notice notice : noticeListB){
           System.out.println(notice.toString());
       }

   }

    @Test
    void ?????????_meetDate_deadline_??????() throws Exception{
        //given
        Member member = createMember();

        MeetingDTO meetingDTO = MeetingDTO.builder()
                .category(Category.SPORTS)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2021,06,04,16,30))
                .reqDeadline(LocalDateTime.of(2021,06,04,17,00))
                .maxNumber(4)
                .build();
        
        //when
        CustomException e = assertThrows(CustomException.class,
                () -> meetingService.register(meetingDTO, member.getId()));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("???????????? ????????????????????? ????????????.");
    }


    @Test
    void ????????????() throws Exception{
        //given
        Meeting meetingA = createMeeting();

        MeetingImg memberImgA = MeetingImg.builder()
                .meeting(meetingA)
                .storeFileName(UUID.randomUUID().toString() + "_MeetingImg1")
                .folderPath("/hyeongwoo1")
                .build();

        meetingImgRepository.save(memberImgA);

        //mock ????????? ?????? meeting.imgFiles ??????
        List<MultipartFile> imgFiles = new ArrayList<>();

        MultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());
        MultipartFile file2 = new MockMultipartFile("file", "filename-2.jpeg", "image/jpeg", "some-image".getBytes());

        imgFiles.add(file1);
        imgFiles.add(file2);

        //MeetingDTO ??????
        MeetingDTO meetingDTO = MeetingDTO.builder()
                .meetingId(meetingA.getId())
                .category(Category.MUSIC) //?????? sports -> music
                .title("MeetingB") //meetingA -> meeting B??? ??????
                .text("meet2") //meet -> meet2
                .place("B") // A -> B
                .meetDate(LocalDateTime.of(2021,10,14,00,00))
                .reqDeadline(LocalDateTime.of(2021,10,10,00,00))
                .maxNumber(5) // 4 -> 5??? ??????
                .imageFiles(imgFiles)
                .build();

        //when
        meetingService.modify(meetingDTO, meetingA.getMember().getId());

        //then
        //????????????
        Assertions.assertThat(meetingA.getTitle()).isEqualTo("MeetingB");
        Assertions.assertThat(meetingA.getMember().getId()).isEqualTo(meetingA.getMember().getId());
        Assertions.assertThat(meetingA.getCategory()).isEqualTo(meetingDTO.getCategory());
        //Assertions.assertThat(meetingA.getDDay()).isEqualTo(5);

        //?????? ?????? ??????
        List<MeetingImg> meetingImgList = meetingImgRepository.findByMeeting(meetingA);
        Assertions.assertThat(meetingImgList.size()).isEqualTo(2);

    }

    @Test
    void ??????????????????() throws Exception{
        //given
        Meeting meeting = createMeeting();

        MeetingDTO meetingDTO = MeetingDTO.builder()
                .meetingId(meeting.getId())
                .category(Category.SPORTS)
                .title("MeetingB") //meetingA -> meeting B??? ??????
                .text("meet2") //meet -> meet2
                .place("B") // A -> B
                .meetDate(LocalDateTime.of(2021,06,05,00,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                .maxNumber(2) // 4 -> 2??? ??????
                .build();

        //when
        CustomException e = assertThrows(CustomException.class,
                () -> meetingService.modify(meetingDTO, meeting.getMember().getId()));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("???????????? ???????????????.");

    }

    @Test
    void ????????????() throws Exception{
        //given
        //????????? ??????
        Participant participant = createParticipant();

        Meeting meeting = participant.getMeeting();
        Long meetingId = meeting.getId();

        MeetingImg meetingImg = MeetingImg.builder()
                .meeting(meeting)
                .storeFileName(UUID.randomUUID().toString() + "_MeetingImg1")
                .folderPath("/hyeongwoo1")
                .build();

        meetingImgRepository.save(meetingImg);

        //meeting ??? ????????? ??????
        Member member =Member.builder()
                .email("memberB@kookmin.ac.kr")
                .nickname("memberBNickname")
                .password("1111")
                .build();
        memberRepository.save(member);

        RequestParticipant requestParticipant = RequestParticipant.builder()
                .meeting(meeting)
                .member(member)
                .build();
        requestParticipantRepository.save(requestParticipant);

        //meeting ?????? ??????
        Comment commentA = createComment(member); //???????????? ?????? ??????.
        commentA.setMeeting(meeting);

        commentRepository.save(commentA);

        Comment commentB = createComment(meeting.getMember());
        commentB.setMeeting(meeting);
        commentB.setParent(commentA);

        commentRepository.save(commentB); //????????? ????????? ????????? ????????? ??????.

        //meeting ?????? ??????
        Heart heartA = createHeart(member); // ???????????? ?????? ??????.
        heartA.setMeeting(meeting);

        heartRepository.save(heartA);

        //when
        meetingService.remove(meetingId);

        //then
        //????????? ????????? ????????? ????????? ?????? (????????? ??????)
        NoSuchElementException e1 = assertThrows(NoSuchElementException.class,
                () -> (participantRepository.findById(participant.getId())).get());

        //????????? ????????? ??????????????? ????????? ?????? (??????????????? ??????)
        NoSuchElementException e2 = assertThrows(NoSuchElementException.class,
                () -> (requestParticipantRepository.findById(requestParticipant.getId())).get());

        //????????? ???????????? ?????? ?????? (???????????? ??????)
        NoSuchElementException e3 = assertThrows(NoSuchElementException.class,
                () -> meetingImgRepository.findById(meetingImg.getId()).get());

        //????????? ???????????? ????????? ?????? (????????? ??????)
        NoSuchElementException e4 = assertThrows(NoSuchElementException.class,
                () -> commentRepository.findById(commentB.getId()).get());

        //????????? ???????????? ?????? (?????? ??????)
        NoSuchElementException e5 = assertThrows(NoSuchElementException.class,
                () -> commentRepository.findById(commentA.getId()).get());

        //????????? ???????????? ?????? (?????? ??????)
        NoSuchElementException e6 = assertThrows(NoSuchElementException.class,
                () -> heartRepository.findById(heartA.getId()).get());

        //????????? ????????? ?????? (?????? ??????)
        NoSuchElementException e7 = assertThrows(NoSuchElementException.class,
                () -> meetingRepository.findById(meetingId).get());


        Assertions.assertThat(e1.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e2.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e3.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e4.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e5.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e6.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e7.getMessage()).isEqualTo("No value present");

        //?????? ?????? -> ?????????????????? ??????
        List<Notice> noticeList = noticeRepository.getNoticeList(participant.getMember().getId());

        for (Notice notice : noticeList){
            System.out.println(notice.toString());
        }

        Assertions.assertThat(noticeList.size()).isEqualTo(1);
    }


    @Test
    void ??????_?????????????????????_?????????x() throws Exception{
        //given
        //meeting ??????
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
                    .storeFileName("231f@Rfl_file1.jpeg")
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
                            LocalDateTime.of(2023,8,04,00,00)
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

        meeting1.changeCategory(Category.MUSIC); // category = "??????"
        meeting2.changeCategory(Category.SPORTS); // category = "?????????"
        meeting3.changeCategory(Category.SPORTS); // category = "?????????"
        meeting4.changeCategory(Category.DANCE); // category = "???"
        meeting5.changeCategory(Category.TRAVEL); // category = "??????"

        meeting2.changeTitle("10????????? ?????? ??????"); // meeting2
        meeting4.changeTitle("????????? ??????"); //meeting4

        //?????? ?????? ?????? meeting2 -> ?????? 2???
        MeetingImg meetingImgA = MeetingImg.builder()
                .meeting(meeting2)
                .folderPath("vine/2021/09/21")
                .storeFileName("231f@Rfl_??????1.jpeg")
                .build();
        meetingImgRepository.save(meetingImgA);

        MeetingImg meetingImgB = MeetingImg.builder()
                .meeting(meeting2)
                .folderPath("vine/2021/09/21")
                .storeFileName("231f@Rfl_??????2.jpeg")
                .build();
        meetingImgRepository.save(meetingImgB);
        
        //?????? ?????? - meeting2 ???????????? ??????
        Comment commentA = createComment(meeting2.getMember());

        commentA.setMeeting(meeting2);

        commentRepository.save(commentA);

        Comment commentB = createComment(meeting1.getMember()); //meeting1 ????????? meeting2??? ?????? ??????

        commentB.setMeeting(meeting2);

        commentB.setParent(commentA);

        commentRepository.save(commentB);

        //?????? ?????? ?????? - meeting ???????????? ?????? - 2???
        Heart heartA = createHeart(meeting2.getMember());
        heartA.setMeeting(meeting2);

        heartRepository.save(heartA);

        Heart heartB = createHeart(meeting1.getMember()); //meeting1 ?????? ????????? ?????? ??????.
        heartB.setMeeting(meeting2);

        heartRepository.save(heartB);


        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .categoryList(List.of(Category.SPORTS, Category.DANCE))
                .keyword("??????")
                .page(1)
                .size(36)
                .build();

        //when
        /**
         * meeting2,3 (?????????), meeting4 (???)
         * meeting2,4 title =  ~??????~
         * = meeting2,4??? ??????
         * meeting2 ?????? ?????? 2??? -> id ?????? ????????? 1?????? ??????
         * meeting4 ?????? ?????? x
         */
        PageResultDTO<MeetingResDTO, Object[]> result = meetingService.getAllMeetingList(pageRequestDTO, null);

        //then
        List<MeetingResDTO> meetingResDTOList = result.getDtoList();

        for (MeetingResDTO meetingResDTO : meetingResDTOList){
            System.out.println(meetingResDTO);
        }

        Assertions.assertThat(result.getPage()).isEqualTo(1);
        Assertions.assertThat(result.getDtoList().size()).isEqualTo(2);
        Assertions.assertThat(result.getTotalPage()).isEqualTo(1);
    }

    @Test
    void ??????_?????????????????????_?????????o() throws Exception{
        //given
        //meeting ??????
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
                    .storeFileName("231f@Rfl_file1.jpeg")
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
                            LocalDateTime.of(2023,8,04,00,00)
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

        meeting1.changeCategory(Category.MUSIC); // category = "??????"
        meeting2.changeCategory(Category.SPORTS); // category = "?????????"
        meeting3.changeCategory(Category.SPORTS); // category = "?????????"
        meeting4.changeCategory(Category.DANCE); // category = "???"
        meeting5.changeCategory(Category.TRAVEL); // category = "??????"

        meeting2.changeTitle("10????????? ?????? ??????"); // meeting2
        meeting4.changeTitle("????????? ??????"); //meeting4

        //?????? ?????? ?????? meeting2 -> ?????? 2???
        MeetingImg meetingImgA = MeetingImg.builder()
                .meeting(meeting2)
                .folderPath("vine/2021/09/21")
                .storeFileName("231f@Rfl_??????1.jpeg")
                .build();
        meetingImgRepository.save(meetingImgA);

        MeetingImg meetingImgB = MeetingImg.builder()
                .meeting(meeting2)
                .folderPath("vine/2021/09/21")
                .storeFileName("231f@Rfl_??????2.jpeg")
                .build();
        meetingImgRepository.save(meetingImgB);

        //?????? ?????? - meeting2 ???????????? ??????
        Comment commentA = createComment(meeting2.getMember());

        commentA.setMeeting(meeting2);

        commentRepository.save(commentA);

        Comment commentB = createComment(meeting1.getMember()); //meeting1 ????????? meeting2??? ?????? ??????

        commentB.setMeeting(meeting2);

        commentB.setParent(commentA);

        commentRepository.save(commentB);

        //?????? ?????? ?????? ??????
        Member memberUser = Member.builder()
                .email("memberUser@kookmin.ac.kr")
                .nickname("memberUserNickname")
                .password("1111")
                .build();
        memberRepository.save(memberUser);

        //memberUser ??? meeting2,5 ??? ????????? ??????
        Bookmark bookmarkA = Bookmark.builder()
                .member(memberUser)
                .build();

        bookmarkA.setMeeting(meeting2);

        bookmarkRepository.save(bookmarkA);

        Bookmark bookmarkB = Bookmark.builder()
                .member(memberUser)
                .build();

        bookmarkB.setMeeting(meeting5);

        bookmarkRepository.save(bookmarkB);

        //memberUser ??? meeting2, 5 ?????? ??????.
        Heart heartA = createHeart(memberUser);
        heartA.setMeeting(meeting2);
        heartRepository.save(heartA);

        Heart heartB = createHeart(memberUser);
        heartB.setMeeting(meeting5);
        heartRepository.save(heartB);

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .categoryList(List.of(Category.SPORTS, Category.DANCE))
                .keyword("??????")
                .page(1)
                .size(36)
                .build();

        //when
        /**
         * memberUser ??? meeting2,5 ????????? ??????
         * meeting2,3 (?????????), meeting4 (???)
         * meeting2,4 title =  ~??????~
         * = meeting2,4??? ?????? -> 2?????? bookmark = true
         * meeting2 ?????? ?????? 2??? -> id ?????? ????????? 1?????? ??????
         * meeting4 ?????? ?????? x
         */
        PageResultDTO<MeetingResDTO, Object[]> result = meetingService.getAllMeetingList(pageRequestDTO, memberUser.getId());

        //then
        List<MeetingResDTO> meetingResDTOList = result.getDtoList();

        for (MeetingResDTO meetingResDTO : meetingResDTOList){
            System.out.println(meetingResDTO);
        }

        Assertions.assertThat(result.getPage()).isEqualTo(1);
        Assertions.assertThat(result.getDtoList().size()).isEqualTo(2);
        Assertions.assertThat(result.getDtoList().get(0).getBookmarkState()).isFalse();
        Assertions.assertThat(result.getDtoList().get(1).getBookmarkState()).isTrue();
        Assertions.assertThat(result.getDtoList().get(1).getHeartState()).isTrue();
        Assertions.assertThat(result.getDtoList().get(1).getHeartCount()).isEqualTo(1);
        Assertions.assertThat(result.getTotalPage()).isEqualTo(1);
    }

    @Test
    void ?????????_???????????????() throws Exception{
        //given
        //meeting ??????
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
                    .storeFileName("231qqf@Rfl_file1.jpeg")
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
                            LocalDateTime.of(2023,6,04,00,00)
                                    .toLocalDate().atStartOfDay()).toDays())
                    .maxNumber(4)
                    .currentNumber(3)
                    .build();
            meetingRepository.save(meeting);

        });

        List<Meeting> meetingList = meetingRepository.findAll();

        Meeting meeting1 = meetingList.get(0);
        Meeting meeting4 = meetingList.get(3);

        /**
         * memberUser ??? meeting1, 4 ????????? ?????????
         */
        Member memberUser = Member.builder()
                .email("memberUser@kookmin.ac.kr")
                .nickname("memberUserNickname")
                .password("1111")
                .build();
        memberRepository.save(memberUser);

        followRepository.rFollow(memberUser.getId(), meeting1.getMember().getId());
        followRepository.rFollow(memberUser.getId(), meeting4.getMember().getId());

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(36)
                .build();

        //when
        PageResultDTO<MeetingResDTO, Object[]> result = meetingService.getFollowMeetingList(pageRequestDTO, memberUser.getId());

        //then
        List<MeetingResDTO> meetingResDTOList = result.getDtoList();

        for (MeetingResDTO meetingResDTO : meetingResDTOList){
            System.out.println(meetingResDTO);
        }

        Assertions.assertThat(result.getPage()).isEqualTo(1);
        Assertions.assertThat(result.getDtoList().size()).isEqualTo(2);
        Assertions.assertThat(result.getTotalPage()).isEqualTo(1);
        Assertions.assertThat(result.getSize()).isEqualTo(36);
    }

    @Test
    void ?????????_???????????????() throws Exception{
        //given
        //meeting ??????
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
                    .storeFileName("231qqf@Rfl_file1.jpeg")
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
                            LocalDateTime.of(2023,6,04,00,00)
                                    .toLocalDate().atStartOfDay()).toDays())
                    .maxNumber(4)
                    .currentNumber(3)
                    .build();
            meetingRepository.save(meeting);

        });

        List<Meeting> meetingList = meetingRepository.findAll();

        Meeting meeting1 = meetingList.get(0);
        Meeting meeting4 = meetingList.get(3);
        Meeting meeting5 = meetingList.get(4);

        //?????? ?????? ?????? ??????
        Member memberUser = Member.builder()
                .email("memberUser@kookmin.ac.kr")
                .nickname("memberUserNickname")
                .password("1111")
                .build();
        memberRepository.save(memberUser);

        //memberUser ??? meeting1,4,5 ??? ????????? ??????
        Bookmark bookmarkA = Bookmark.builder()
                .member(memberUser)
                .build();

        bookmarkA.setMeeting(meeting1);

        bookmarkRepository.save(bookmarkA);

        Bookmark bookmarkB = Bookmark.builder()
                .member(memberUser)
                .build();

        bookmarkB.setMeeting(meeting4);

        bookmarkRepository.save(bookmarkB);

        Bookmark bookmarkC = Bookmark.builder()
                .member(memberUser)
                .build();

        bookmarkC.setMeeting(meeting5);

        bookmarkRepository.save(bookmarkC);

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(36)
                .build();

        //when
        PageResultDTO<MeetingResDTO, Object[]> result = meetingService.getBookmarkMeetingList(
                pageRequestDTO, memberUser.getId());

        //then
        List<MeetingResDTO> meetingResDTOList = result.getDtoList();

        for (MeetingResDTO meetingResDTO : meetingResDTOList){
            System.out.println(meetingResDTO);
        }

        Assertions.assertThat(result.getPage()).isEqualTo(1);
        Assertions.assertThat(result.getDtoList().size()).isEqualTo(3);
        Assertions.assertThat(result.getTotalPage()).isEqualTo(1);
        Assertions.assertThat(result.getSize()).isEqualTo(36);
    }


    @Test
    void ??????_?????????_????????????() throws Exception{
        //given
        Member member = createMember();


        /**
         * Meeting ??????
         * ?????? ????????????
         * meetingA dDay : -1 > meetingB dDay : 0 > meetingC dDay : 1
         * ?????? : meetingB > meetingC (2?????? ??????)
         */
        Meeting meetingA = Meeting.builder()
                .category(Category.SPORTS)
                .member(member)
                .title("Meeting")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2023, 8, 01, 00, 00))
                .reqDeadline(LocalDateTime.of(2023, 06, 05, 00, 00))
                .dDay(-1L)
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meetingA);

        Meeting meetingB = Meeting.builder()
                .category(Category.SPORTS)
                .member(member)
                .title("Meeting")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2023, 8, 01, 00, 00))
                .reqDeadline(LocalDateTime.of(2023, 06, 04, 00, 00))
                .dDay(0L)
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meetingB);

        Meeting meetingC = Meeting.builder()
                .category(Category.SPORTS)
                .member(member)
                .title("Meeting")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2023, 8, 01, 00, 00))
                .reqDeadline(LocalDateTime.of(2023, 06, 06, 00, 00))
                .dDay(1L)
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meetingC);

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(36)
                .sort(List.of("reqDeadline", "ASC"))
                .build();

        //when
        PageResultDTO<MeetingResDTO, Object[]> result = meetingService.getAllMeetingList(pageRequestDTO, meetingA.getMember().getId());

        //then
        List<MeetingResDTO> meetingResDTOList = result.getDtoList();

        for (MeetingResDTO meetingResDTO : meetingResDTOList){
            System.out.println(meetingResDTO);
        }

        Assertions.assertThat(result.getPage()).isEqualTo(1);
        Assertions.assertThat(result.getDtoList().size()).isEqualTo(2);
        Assertions.assertThat(meetingResDTOList.get(0).getMeetingId()).isEqualTo(meetingB.getId());
        Assertions.assertThat(result.getTotalPage()).isEqualTo(1);
    }

    @Test
    void ??????_???????????????DTO() throws Exception{
        //given
        Member master = createMember();

        MeetingDTO meetingDTO = MeetingDTO.builder()
                .category(Category.SPORTS)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2021,06,05,00,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                .maxNumber(4)
                .build();


        Long meetingId = meetingService.register(meetingDTO, master.getId());

        Meeting meeting = meetingRepository.findById(meetingId).get();

        //?????? ????????? ?????? ??????
        MemberImg masterImg = MemberImg.builder()
                .member(master)
                .storeFileName(UUID.randomUUID().toString() + "_masterImg1")
                .folderPath("/hyeongwoo1")
                .build();

        memberImgRepository.save(masterImg);

        //?????? ?????? 2??? ??????
        MeetingImg meetingImg1 = MeetingImg.builder()
                .meeting(meeting)
                .storeFileName(UUID.randomUUID().toString() + "_MeetingImg1")
                .folderPath("/hyeongwoo1")
                .build();

        meetingImgRepository.save(meetingImg1);

        MeetingImg meetingImg2 = MeetingImg.builder()
                .meeting(meeting)
                .storeFileName(UUID.randomUUID().toString() + "_MeetingImg2")
                .folderPath("/hyeongwoo1")
                .build();

        meetingImgRepository.save(meetingImg2);

        //?????? ?????? - commentA??? ???????????? commentB
        Comment commentA = createComment(meeting.getMember());

        commentA.setMeeting(meeting);

        commentRepository.save(commentA);

        Comment commentB = createComment(meeting.getMember());

        commentB.setMeeting(meeting);

        commentB.setParent(commentA);

        commentRepository.save(commentB);

        //????????? ?????? - 5???
        IntStream.rangeClosed(1,5).forEach(i -> {

            Member member = Member.builder()
                    .email("member"+i+"@kookmin.ac.kr")
                    .nickname("member"+i+"Nickname")
                    .text("????????????")
                    .password("1111")
                    .build();
            memberRepository.save(member);

            Participant participant = Participant.builder()
                    .member(member)
                    .meeting(meeting)
                    .build();

            participantRepository.save(participant);

            //????????? ????????? ?????? ??????
            MemberImg memberImg = MemberImg.builder()
                    .member(member)
                    .folderPath("vine/2021/09/21")
                    .storeFileName("123Rfl_file1.jpeg")
                    .build();
            memberImgRepository.save(memberImg);
        });

        //?????? ?????? ?????? ??????
        Member memberUser = Member.builder()
                .email("memberUser@kookmin.ac.kr")
                .nickname("memberUserNickname")
                .password("1111")
                .build();
        memberRepository.save(memberUser);

        //?????? ??????
        Heart heartA = createHeart(memberUser); //?????? ???????????? ?????? ??????
        heartA.setMeeting(meeting);
        heartRepository.save(heartA);

        Heart heartB = createHeart(meeting.getMember());//?????? ????????? ?????? ??????
        heartB.setMeeting(meeting);
        heartRepository.save(heartB);


        //?????? ?????? ????????? ?????? - meeting ????????? ?????????
        Bookmark bookmark = Bookmark.builder()
                .member(memberUser)
                .build();

        bookmark.setMeeting(meeting);

        bookmarkRepository.save(bookmark);

        //?????? ?????? ?????? ????????? ??????
        Participant participant = Participant.builder()
                .member(memberUser)
                .meeting(meeting)
                .build();

        participantRepository.save(participant);


        //when
        DetailMeetingDTO detailMeetingDTO = meetingService.getMeeting(meeting.getId(), memberUser.getId());
        //then
        //?????? ??????
        Assertions.assertThat(detailMeetingDTO.getMeetingId()).isEqualTo(meeting.getId());
        Assertions.assertThat(detailMeetingDTO.getMasterId()).isEqualTo(master.getId());
        Assertions.assertThat(detailMeetingDTO.getCategory()).isEqualTo(meeting.getCategory());
        Assertions.assertThat(detailMeetingDTO.getCommentCount()).isEqualTo(2);
        Assertions.assertThat(detailMeetingDTO.getHeartCount()).isEqualTo(2);
        Assertions.assertThat(detailMeetingDTO.getHeartState()).isTrue();
        Assertions.assertThat(detailMeetingDTO.getParticipantId()).isEqualTo(participant.getId());
        Assertions.assertThat(detailMeetingDTO.getBookmarkState()).isTrue();
        Assertions.assertThat(detailMeetingDTO.getDDay()).isEqualTo(meeting.getDDay());

        //?????? ?????? ??????
        Assertions.assertThat(detailMeetingDTO.getImgDTOList().size()).isEqualTo(2);


        //????????? ????????? ??????
        List<ParticipantDTO> participantDTOList = detailMeetingDTO.getParticipantDTOList();
        for(ParticipantDTO participantDTO : participantDTOList){
            System.out.println(participantDTO);
        }

        // ????????? ??? 1??????, ????????? 5??? ??????, ?????? ?????? ?????? 1??????
        Assertions.assertThat(detailMeetingDTO.getParticipantDTOList().size()).isEqualTo(7);
    }

    @Test
    void ??????_???????????????DTO_??????x() throws Exception{
        //given
        //?????? ?????? - ?????? ?????? x
        Member master = createMember();

        MeetingDTO meetingDTO = MeetingDTO.builder()
                .category(Category.SPORTS)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2021,06,05,00,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                .maxNumber(4)
                .build();


        Long meetingId = meetingService.register(meetingDTO, master.getId());

        Meeting meeting = meetingRepository.findById(meetingId).get();

        //?????? ?????? ?????? ?????? - meeting ?????? ????????? x
        Member memberUser = Member.builder()
                .email("memberUser@kookmin.ac.kr")
                .nickname("memberUserNickname")
                .password("1111")
                .build();
        memberRepository.save(memberUser);

        /**
         * ???????????? x , ?????? ??????????????? x, ?????? x, ????????? x, ????????? - ?????? ????????? x
         */
        //when
        DetailMeetingDTO detailMeetingDTO = meetingService.getMeeting(meeting.getId(), memberUser.getId());

        //then
        //?????? ??????
        Assertions.assertThat(detailMeetingDTO.getMeetingId()).isEqualTo(meeting.getId());
        Assertions.assertThat(detailMeetingDTO.getMasterId()).isEqualTo(meeting.getMember().getId());
        Assertions.assertThat(detailMeetingDTO.getCategory()).isEqualTo(meeting.getCategory());
        Assertions.assertThat(detailMeetingDTO.getParticipantId()).isEqualTo(null);
        Assertions.assertThat(detailMeetingDTO.getCommentCount()).isEqualTo(0);
        Assertions.assertThat(detailMeetingDTO.getBookmarkState()).isFalse();
        Assertions.assertThat(detailMeetingDTO.getDDay()).isEqualTo(meeting.getDDay());

        //?????? ?????? ??????
        Assertions.assertThat(detailMeetingDTO.getImgDTOList().size()).isEqualTo(0);

        //????????? ????????? ??????
        List<ParticipantDTO> participantDTOList = detailMeetingDTO.getParticipantDTOList();
        Assertions.assertThat(participantDTOList.size()).isEqualTo(1);

        Assertions.assertThat(participantDTOList.size()).isEqualTo(meeting.getCurrentNumber());
    }

    @Test
    void dDay_??????() throws Exception{
        //given
        Member member = createMember();

        IntStream.rangeClosed(1,5).forEach(i -> {
            Meeting meeting = Meeting.builder()
                    .category(Category.SPORTS)
                    .member(member)
                    .title("Meeting" + i)
                    .text("meet")
                    .place("A")
                    .meetDate(LocalDateTime.of(2021,06,05,00,00)) //?????? ??????
                    .reqDeadline(LocalDateTime.of(2021,06,04,00,00)) //?????? ??????
                    .dDay((long)i-2)
                    .maxNumber(4)
                    .currentNumber(3)
                    .build();

            meetingRepository.save(meeting);
        });

        //when
        meetingService.updatedDay();

        //then
        List<Meeting> result = meetingRepository.findAll();

        System.out.println("===============================");
        for (Meeting meeting : result){
            System.out.println(meeting.getDDay());
        }

        Assertions.assertThat(result.get(0).getDDay()).isEqualTo(-1);
        Assertions.assertThat(result.get(1).getDDay()).isEqualTo(-1);
        Assertions.assertThat(result.get(2).getDDay()).isEqualTo(0);
        Assertions.assertThat(result.get(3).getDDay()).isEqualTo(1);
        Assertions.assertThat(result.get(4).getDDay()).isEqualTo(2);
    }

    private Heart createHeart(Member member) {

        Heart heart = Heart.builder()
                .member(member)
                .build();

        return heart;
    }

    private Comment createComment(Member member) {

        Comment comment = Comment.builder()
                .member(member)
                .content("????????????!")
                .open(true)
                .build();

        return comment;
    }

    private Participant createParticipant() {
        Meeting meeting = createMeeting();
        Member member =Member.builder()
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
                .build();

        memberRepository.save(member);

        Participant participant = Participant.builder()
                .meeting(meeting)
                .member(member)
                .build();

        participantRepository.save(participant);

        return participant;
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
                .email("member@kookmin.ac.kr")
                .nickname("memberNickname")
                .password("1111")
                .build();

        memberRepository.save(member);

        return member;
    }
}