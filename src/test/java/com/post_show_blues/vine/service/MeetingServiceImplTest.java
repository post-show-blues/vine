package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.domain.comment.CommentRepository;
import com.post_show_blues.vine.domain.follow.FollowRepository;
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
import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
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


   @Test
    void 모임등록() throws Exception{
        //given
        Member memberA = createMember();

        //memberA를 팔로우 하는 memberB,C 생성
       Member memberB = Member.builder()
               .name("memberB")
               .email("memberB@kookmin.ac.kr")
               .nickname("memberNicknameB")
               .password("1111")
               .phone("010-0000-0000")
               .university("국민대학교")
               .build();

       memberRepository.save(memberB);

       Member memberC = Member.builder()
               .name("memberC")
               .email("memberC@kookmin.ac.kr")
               .nickname("memberNicknameC")
               .password("1111")
               .phone("010-0000-0000")
               .university("국민대학교")
               .build();

       memberRepository.save(memberC);

       //memberB,C 가 A를 팔로우 -> A가 모임생성시 B,C에게 알림 생성
       followRepository.rFollow(memberB.getId(), memberA.getId());
       followRepository.rFollow(memberC.getId(), memberA.getId());

       //mock 이미지 파일 meeting.imgFiles 생성
        List<MultipartFile> imgFiles = new ArrayList<>();

        MultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());
        MultipartFile file2 = new MockMultipartFile("file", "filename-2.jpeg", "image/jpeg", "some-image".getBytes());

        imgFiles.add(file1);
        imgFiles.add(file2);


        //meetingDTO 생성
        MeetingDTO meetingDTO = MeetingDTO.builder()
                .category(Category.SPORTS)
                .masterId(memberA.getId())
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2021,07,13,16,00))
                .reqDeadline(LocalDateTime.of(2021,06,05,16,00))
                .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                        LocalDateTime.of(2021,06,05,00,00)
                                .toLocalDate().atStartOfDay()).toDays())
                .maxNumber(4)
                .imageFiles(imgFiles) //이미지 파일
                .build();

        //when
        Long saveId = meetingService.register(meetingDTO);

        //then
        //모임 검증
        Meeting meeting = meetingService.findOne(saveId);
        Assertions.assertThat(meeting.getTitle()).isEqualTo("MeetingA");
        Assertions.assertThat(meeting.getMember().getId()).isEqualTo(memberA.getId());
        Assertions.assertThat(meeting.getCategory()).isEqualTo(meetingDTO.getCategory());
        Assertions.assertThat(meeting.getCurrentNumber()).isEqualTo(0);
        //Assertions.assertThat(meeting.getDDay()).isEqualTo(2);

        //모임 imgFiles 검증
        List<MeetingImg> meetingImgList = meetingImgRepository.findByMeeting(meeting);
        Assertions.assertThat(meetingImgList.size()).isEqualTo(2);

        //알림 검증
       List<Notice> noticeListB = noticeRepository.getNoticeList(memberB.getId());
       List<Notice> noticeListC = noticeRepository.getNoticeList(memberC.getId());

       Assertions.assertThat(noticeListB.size()).isEqualTo(1);
       Assertions.assertThat(noticeListC.size()).isEqualTo(1);

       for (Notice notice : noticeListB){
           System.out.println(notice.toString());
       }

   }

    @Test
    void 등록시_meetDate_deadline_비교() throws Exception{
        //given
        Member member = createMember();

        MeetingDTO meetingDTO = MeetingDTO.builder()
                .category(Category.SPORTS)
                .masterId(member.getId())
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2021,06,04,16,30))
                .reqDeadline(LocalDateTime.of(2021,06,04,17,00))
                .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                        LocalDateTime.of(2021,06,05,00,00)
                                .toLocalDate().atStartOfDay()).toDays())
                .maxNumber(4)
                .build();
        
        //when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> meetingService.register(meetingDTO));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("활동일이 신청마감일보다 빠릅니다.");
    }


    @Test
    void 모임수정() throws Exception{
        //given
        Meeting meetingA = createMeeting();

        MeetingImg memberImgA = MeetingImg.builder()
                .meeting(meetingA)
                .storeFileName(UUID.randomUUID().toString() + "_MeetingImg1")
                .folderPath("/hyeongwoo1")
                .build();

        meetingImgRepository.save(memberImgA);

        //수정 데이터
        Member memberB = Member.builder()
                .name("member")
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(memberB);

        //mock 이미지 파일 meeting.imgFiles 생성
        List<MultipartFile> imgFiles = new ArrayList<>();

        MultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());
        MultipartFile file2 = new MockMultipartFile("file", "filename-2.jpeg", "image/jpeg", "some-image".getBytes());

        imgFiles.add(file1);
        imgFiles.add(file2);

        //MeetingDTO 생성
        MeetingDTO meetingDTO = MeetingDTO.builder()
                .meetingId(meetingA.getId())
                .category(Category.MUSIC) //변경 sports -> music
                .masterId(memberB.getId()) //변경
                .title("MeetingB") //meetingA -> meeting B로 변경
                .text("meet2") //meet -> meet2
                .place("B") // A -> B
                .meetDate(LocalDateTime.of(2021,07,14,00,00)) //5 -> 6일로 변경
                .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                        LocalDateTime.of(2021,06,05,00,00)
                                .toLocalDate().atStartOfDay()).toDays())
                .maxNumber(5) // 4 -> 5로 변경
                .imageFiles(imgFiles)
                .build();

        //when
        meetingService.modify(meetingDTO);

        //then
        //모임수정
        Assertions.assertThat(meetingA.getTitle()).isEqualTo("MeetingB");
        Assertions.assertThat(meetingA.getMember().getId()).isEqualTo(memberB.getId());
        Assertions.assertThat(meetingA.getCategory()).isEqualTo(meetingDTO.getCategory());
        //Assertions.assertThat(findMeeting.getDDay()).isEqualTo(3);

        //모임 사진 수정
        List<MeetingImg> meetingImgList = meetingImgRepository.findByMeeting(meetingA);
        Assertions.assertThat(meetingImgList.size()).isEqualTo(2);

    }

    @Test
    void 인원수정오류() throws Exception{
        //given
        Meeting meeting = createMeeting();

        //수정 데이터
        Member member1 = Member.builder()
                .name("member")
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(member1);

        MeetingDTO meetingDTO = MeetingDTO.builder()
                .meetingId(meeting.getId())
                .category(Category.SPORTS)
                .masterId(member1.getId())
                .title("MeetingB") //meetingA -> meeting B로 변경
                .text("meet2") //meet -> meet2
                .place("B") // A -> B
                .meetDate(LocalDateTime.of(2021,06,05,00,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,00,00))
                .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                        LocalDateTime.of(2021,06,05,00,00)
                                .toLocalDate().atStartOfDay()).toDays())
                .maxNumber(2) // 4 -> 2로 변경
                .build();

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> meetingService.modify(meetingDTO));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("참여인원 초과입니다.");

    }

    @Test
    void 모임삭제() throws Exception{
        //given
        //참여자 생성
        Participant participant = createParticipant();

        Meeting meeting = participant.getMeeting();
        Long meetingId = meeting.getId();

        MeetingImg meetingImg = MeetingImg.builder()
                .meeting(meeting)
                .storeFileName(UUID.randomUUID().toString() + "_MeetingImg1")
                .folderPath("/hyeongwoo1")
                .build();

        meetingImgRepository.save(meetingImg);

        //meeting 의 요청자 생성
        Member member =Member.builder()
                .name("member")
                .email("memberB@kookmin.ac.kr")
                .nickname("memberBNickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();
        memberRepository.save(member);

        RequestParticipant requestParticipant = RequestParticipant.builder()
                .meeting(meeting)
                .member(member)
                .build();
        requestParticipantRepository.save(requestParticipant);

        //meeting 댓글 생성
        Comment commentA = createComment(member); //참여자가 댓글 남김.
        commentA.setMeeting(meeting);

        commentRepository.save(commentA);

        Comment commentB = createComment(meeting.getMember());
        commentB.setMeeting(meeting);
        commentB.setParent(commentA);

        commentRepository.save(commentB); //방장이 참여자 댓글에 대댓글 남김.

        //when
        meetingService.remove(meetingId);

        //then
        //삭제된 모임에 참여된 레코드 검색 (참여자 삭제)
        NoSuchElementException e1 = assertThrows(NoSuchElementException.class,
                () -> (participantRepository.findById(participant.getId())).get());

        //삭제된 모임에 참여요청한 레코드 검색 (참여요청자 삭제)
        NoSuchElementException e2 = assertThrows(NoSuchElementException.class,
                () -> (requestParticipantRepository.findById(requestParticipant.getId())).get());

        //삭제된 모임방의 사진 검색 (모임사진 삭제)
        NoSuchElementException e3 = assertThrows(NoSuchElementException.class,
                () -> meetingImgRepository.findById(meetingImg.getId()).get());

        //삭제된 모임방의 대댓글 검색 (대댓글 삭제)
        NoSuchElementException e4 = assertThrows(NoSuchElementException.class,
                () -> commentRepository.findById(commentB.getId()).get());

        //삭제된 모임방의 댓글 (댓글 삭제)
        NoSuchElementException e5 = assertThrows(NoSuchElementException.class,
                () -> commentRepository.findById(commentA.getId()).get());

        //삭제된 모임방 검색 (모임 삭제)
        NoSuchElementException e6 = assertThrows(NoSuchElementException.class,
                () -> meetingRepository.findById(meetingId).get());


        Assertions.assertThat(e1.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e2.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e3.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e4.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e5.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e6.getMessage()).isEqualTo("No value present");

    }

    @Test
    void 전체_모임리스트조회() throws Exception{
        //given

        //meeting 생성
        IntStream.rangeClosed(1,5).forEach(i -> {

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
                    .folderPath("vine/2021/09/21")
                    .storeFileName("123Rfl_file1.jpeg")
                    .build();
            memberImgRepository.save(memberImg);

            Participant participant = Participant.builder()
                    .meeting(meeting2)
                    .member(member)
                    .build();

            participantRepository.save(participant);
        });
        
        //댓글 생성 - meeting2 모임에만 생성
        Comment commentA = createComment(meeting2.getMember());

        commentA.setMeeting(meeting2);

        commentRepository.save(commentA);

        Comment commentB = createComment(meeting1.getMember()); //meeting1 방장이 meeting2에 댓글 작성

        commentB.setMeeting(meeting2);

        commentB.setParent(commentA);

        commentRepository.save(commentB);


        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .categoryList(List.of(Category.SPORTS, Category.DANCE))
                .keyword("풋살")
                .page(1)
                .size(36)
                .build();

        //when
        /**
         * meeting2,3 (스포츠), meeting4 (춤)
         * meeting2,4 title =  ~풋살~
         * = meeting2,4만 출력
         * meeting2 참여자 3
         * meeting4 참여자 x
         */
        PageResultDTO<MeetingDTO, Object[]> result = meetingService.getAllMeetingList(pageRequestDTO);

        //then
        List<MeetingDTO> meetingDTOList = result.getDtoList();

        for (MeetingDTO meetingDTO : meetingDTOList){
            System.out.println(meetingDTO);
        }

        Assertions.assertThat(result.getPage()).isEqualTo(1);
        Assertions.assertThat(result.getDtoList().size()).isEqualTo(2);
        Assertions.assertThat(result.getTotalPage()).isEqualTo(1);
    }

    @Test
    void 전체_모임리스트조회_참여자사진X() throws Exception{
        //given
        //meeting 생성
        IntStream.rangeClosed(1,5).forEach(i -> {

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
                            LocalDateTime.of(2023,8,05,00,00)
                                    .toLocalDate().atStartOfDay()).toDays())
                    .maxNumber(4)
                    .currentNumber(3)
                    .build();
            meetingRepository.save(meeting);

        });

        List<Meeting> meetingList = meetingRepository.findAll();

        //첫번째 미팅
        Meeting meeting = meetingList.get(0);

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

            Participant participant = Participant.builder()
                    .meeting(meeting)
                    .member(member)
                    .build();

            participantRepository.save(participant);
        });

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(36)
                .build();

        //when
        /**
            첫번째 모임에만 참여자가 존재하며 프로필 사진은 x
         */
        PageResultDTO<MeetingDTO, Object[]> result = meetingService.getAllMeetingList(pageRequestDTO);

        //then
        List<MeetingDTO> meetingDTOList = result.getDtoList();

        for (MeetingDTO meetingDTO : meetingDTOList){
            System.out.println(meetingDTO);
        }

        Assertions.assertThat(result.getPage()).isEqualTo(1);
        Assertions.assertThat(result.getDtoList().size()).isEqualTo(5);
        Assertions.assertThat(result.getTotalPage()).isEqualTo(1);
    }

    @Test
    void 팔로우_모임리스트() throws Exception{
        //given
        //meeting 생성
        IntStream.rangeClosed(1,5).forEach(i -> {

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
                            LocalDateTime.of(2023,8,05,00,00)
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
         * memberUser 가 meeting1, 4 방장을 팔로우
         */
        Member memberUser = Member.builder()
                .name("memberUser")
                .email("memberUser@kookmin.ac.kr")
                .nickname("memberUserNickname")
                .password("1111")
                .phone("010-1111-1111")
                .university("국민대학교")
                .build();
        memberRepository.save(memberUser);

        followRepository.rFollow(memberUser.getId(), meeting1.getMember().getId());
        followRepository.rFollow(memberUser.getId(), meeting4.getMember().getId());

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(36)
                .build();

        //when
        PageResultDTO<MeetingDTO, Object[]> result = meetingService.getFollowMeetingList(pageRequestDTO, memberUser.getId());

        //then
        List<MeetingDTO> meetingDTOList = result.getDtoList();

        for (MeetingDTO meetingDTO : meetingDTOList){
            System.out.println(meetingDTO);
        }

        Assertions.assertThat(result.getPage()).isEqualTo(1);
        Assertions.assertThat(result.getDtoList().size()).isEqualTo(2);
        Assertions.assertThat(result.getTotalPage()).isEqualTo(1);
        Assertions.assertThat(result.getSize()).isEqualTo(36);
    }

    @Test
    void 정렬_활동일_가까운순() throws Exception{
        //given
        Member member = createMember();


        /**
         * Meeting 생성
         * 활동 가까운순
         * meetingA dDay : -1 > meetingB dDay : 0 > meetingC dDay : 1
         * 출력 : meetingB > meetingC (2개만 출력)
         */
        Meeting meetingA = Meeting.builder()
                .category(Category.SPORTS)
                .member(member)
                .title("Meeting")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2023, 8, 01, 00, 00))
                .reqDeadline(LocalDateTime.of(2023, 06, 04, 00, 00))
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
                .meetDate(LocalDateTime.of(2023, 8, 02, 00, 00))
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
                .meetDate(LocalDateTime.of(2023, 8, 03, 00, 00))
                .reqDeadline(LocalDateTime.of(2023, 06, 04, 00, 00))
                .dDay(1L)
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meetingC);

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(36)
                .sort(List.of("meetDate", "ASC"))
                .build();

        //when
        PageResultDTO<MeetingDTO, Object[]> result = meetingService.getAllMeetingList(pageRequestDTO);

        //then
        List<MeetingDTO> meetingDTOList = result.getDtoList();

        for (MeetingDTO meetingDTO : meetingDTOList){
            System.out.println(meetingDTO);
        }

        Assertions.assertThat(result.getPage()).isEqualTo(1);
        Assertions.assertThat(result.getDtoList().size()).isEqualTo(2);
        Assertions.assertThat(meetingDTOList.get(0).getMeetingId()).isEqualTo(meetingB.getId());
        Assertions.assertThat(result.getTotalPage()).isEqualTo(1);
    }

    @Test
    void 모임_조회페이지DTO() throws Exception{
        //given
        Meeting meeting = createMeeting();

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

        //댓글 생성 - commentA의 대댓글은 commentB
        Comment commentA = createComment(meeting.getMember());

        commentA.setMeeting(meeting);

        commentRepository.save(commentA);

        Comment commentB = createComment(meeting.getMember());

        commentB.setMeeting(meeting);

        commentB.setParent(commentA);

        commentRepository.save(commentB);


        //when
        MeetingDTO meetingDTO = meetingService.getMeeting(meeting.getId());

        //then
        Assertions.assertThat(meetingDTO.getMeetingId()).isEqualTo(meeting.getId());
        Assertions.assertThat(meetingDTO.getCategory()).isEqualTo(meeting.getCategory());
        Assertions.assertThat(meetingDTO.getImgDTOList().size()).isEqualTo(2);
        Assertions.assertThat(meetingDTO.getCommentList().size()).isEqualTo(1);
        Assertions.assertThat(meetingDTO.getCommentList().get(0).getChild().size()).isEqualTo(1);
        Assertions.assertThat(meetingDTO.getCommentCount()).isEqualTo(2);
    }

    @Test
    void 모임_조회페이지DTO_사진x() throws Exception{
        //given
        Meeting meeting = createMeeting();

        //when
        MeetingDTO meetingDTO = meetingService.getMeeting(meeting.getId());

        //then
        Assertions.assertThat(meetingDTO.getMeetingId()).isEqualTo(meeting.getId());
        Assertions.assertThat(meetingDTO.getCategory()).isEqualTo(meeting.getCategory());
        Assertions.assertThat(meetingDTO.getImgDTOList().size()).isEqualTo(0);
    }

    @Test
    void dDay_갱신() throws Exception{
        //given
        Member member = createMember();

        IntStream.rangeClosed(1,5).forEach(i -> {
            Meeting meeting = Meeting.builder()
                    .category(Category.SPORTS)
                    .member(member)
                    .title("Meeting" + i)
                    .text("meet")
                    .place("A")
                    .meetDate(LocalDateTime.of(2021, 8, i, 00, 00))
                    .reqDeadline(LocalDateTime.of(2021, 05, 04, 00, 00))
                    .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                            LocalDateTime.of(2021, 7, i+10, 00, 00)
                                    .toLocalDate().atStartOfDay()).toDays())
                    .maxNumber(4)
                    .currentNumber(3)
                    .build();

            meetingRepository.save(meeting);
        });

        //when
        meetingService.updatedDay();

        //then
        List<Meeting> result = meetingRepository.findAll();

        Assertions.assertThat(result.get(4).getDDay() - result.get(3).getDDay()).isEqualTo(1);
        Assertions.assertThat(result.get(3).getDDay() - result.get(2).getDDay()).isEqualTo(1);
        Assertions.assertThat(result.get(2).getDDay() - result.get(1).getDDay()).isEqualTo(1);
        Assertions.assertThat(result.get(1).getDDay() - result.get(0).getDDay()).isEqualTo(1);

        for (Meeting meeting : result){
            System.out.println(meeting.getDDay());
        }
    }


    private Comment createComment(Member member) {

        Comment comment = Comment.builder()
                .member(member)
                .content("기대돼요!")
                .build();

        return comment;
    }

    private Participant createParticipant() {
        Meeting meeting = createMeeting();
        Member member =Member.builder()
                .name("member")
                .email("memberA@kookmin.ac.kr")
                .nickname("memberANickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
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
                .name("member")
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