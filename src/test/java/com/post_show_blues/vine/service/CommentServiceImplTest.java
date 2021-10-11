package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.domain.comment.CommentRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.dto.comment.CommentDTO;
import com.post_show_blues.vine.dto.comment.CommentReadDTO;
import com.post_show_blues.vine.dto.comment.CommentResDTO;
import com.post_show_blues.vine.service.comment.CommentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@SpringBootTest
public class CommentServiceImplTest {

    @Autowired CommentService commentService;
    @Autowired MemberRepository memberRepository;
    @Autowired MeetingRepository meetingRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired MemberImgRepository memberImgRepository;


    @Test
    void 댓글쓰기() throws Exception {
        //given

        //모임 생성
        Meeting meeting = createMeeting();

        //작성자 생성
        Member writer = Member.builder()
                .name("memberB")
                .email("memberB@kookmin.ac.kr")
                .nickname("memberNicknameB")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(writer);

        //commentDTO 생성
        CommentDTO commentDTO = CommentDTO.builder()
                .memberId(writer.getId())
                .meetingId(meeting.getId())
                .content("정말 기대돼요!")
                .open(true)
                .build();

        //when
        Comment comment = commentService.register(commentDTO, writer.getId());

        //then
        Assertions.assertThat(comment.getId()).isNotNull();
        Assertions.assertThat(comment.getMeeting().getId()).isEqualTo(meeting.getId());
        Assertions.assertThat(comment.getMember().getId()).isEqualTo(writer.getId());
        Assertions.assertThat(comment.getContent()).isEqualTo(commentDTO.getContent());

    }

    @Test
    void 대댓글쓰기() throws Exception{
        //given
        //모임 생성
        Meeting meeting = createMeeting();

        //부모댓글 생성
        Comment parentComment = Comment.builder()
                .meeting(meeting)
                .member(meeting.getMember()) //모임장이 댓글 작성
                .content("많이 참여해주세요!")
                .open(true)
                .build();

        commentRepository.save(parentComment);

        //작성자 생성
        Member writer = Member.builder()
                .name("memberC")
                .email("memberC@kookmin.ac.kr")
                .nickname("memberNicknameC")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(writer);

        //commentDTO 생성
        CommentDTO commentDTO = CommentDTO.builder()
                .memberId(writer.getId())
                .meetingId(meeting.getId())
                .parentId(parentComment.getId())
                .content("정말 기대돼요!")
                .open(true)
                .build();

        //when
        Comment comment = commentService.register(commentDTO, writer.getId());

        //then
        Assertions.assertThat(comment.getId()).isNotNull();
        Assertions.assertThat(comment.getMeeting().getId()).isEqualTo(meeting.getId());
        Assertions.assertThat(comment.getMember().getId()).isEqualTo(writer.getId());
        Assertions.assertThat(comment.getContent()).isEqualTo(commentDTO.getContent());
        Assertions.assertThat(comment.getParent().getId()).isEqualTo(parentComment.getId());
    }

    @Test
    void 대댓글쓰기_부모댓글_비공개() throws Exception{
        //given
        //모임 생성
        Meeting meeting = createMeeting();

        //부모댓글 생성
        Comment parentComment = Comment.builder()
                .meeting(meeting)
                .member(meeting.getMember()) //모임장이 댓글 작성
                .content("많이 참여해주세요!")
                .open(false)
                .build();

        commentRepository.save(parentComment);

        //작성자 생성
        Member writer = Member.builder()
                .name("memberC")
                .email("memberC@kookmin.ac.kr")
                .nickname("memberNicknameC")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(writer);

        //commentDTO 생성
        CommentDTO commentDTO = CommentDTO.builder()
                .memberId(writer.getId())
                .meetingId(meeting.getId())
                .parentId(parentComment.getId())
                .content("정말 기대돼요!")
                .open(true) //true 로 해도 강제적으로 false
                .build();

        //when
        Comment childComment = commentService.register(commentDTO, writer.getId());

        //then
        Assertions.assertThat(childComment.getOpen()).isFalse();
        Assertions.assertThat(parentComment.getOpen()).isFalse();
    }

    @Test
    void 댓글수정() throws Exception{
        //given
        //meeting 생성
        Meeting meeting = createMeeting();

        //comment 생성
        Comment comment = createComment(meeting.getMember()); //방장이 댓글 남김.
        comment.setMeeting(meeting);
        commentRepository.save(comment);

        //commentDTO 생성
        CommentDTO commentDTO = CommentDTO.builder()
                .content("댓글 수정했어요!")
                .open(true)
                .build();

        //when
        commentService.modify(comment.getId(), commentDTO.getContent());

        //then
        Assertions.assertThat(comment.getContent()).isEqualTo(commentDTO.getContent());
        Assertions.assertThat(comment.getMeeting()).isEqualTo(meeting);
        Assertions.assertThat(comment.getMember()).isEqualTo(meeting.getMember());
    }

    @Test
    void 댓글삭제() throws Exception{
        //given
        //meeting 생성
        Meeting meeting = createMeeting();

        //comment 생성
        Comment comment = createComment(meeting.getMember()); //방장이 댓글 남김.
        comment.setMeeting(meeting);
        commentRepository.save(comment);

        //when
        commentService.remove(comment.getId());

        //then
        Assertions.assertThat(comment.getExistState()).isFalse();
    }

    @Test
    void 댓글삭제_대댓글존재o() throws Exception{
        //given
        //meeting 생성
        Meeting meeting = createMeeting();

        //parentComment 생성
        Comment parentComment = createComment(meeting.getMember()); //방장이 댓글 남김.
        parentComment.setMeeting(meeting);
        commentRepository.save(parentComment);

        //childComment 생성
        Comment childComment = createComment(meeting.getMember()); //방장이 대댓글 남김.
        childComment.setMeeting(meeting);
        childComment.setParent(parentComment);
        commentRepository.save(childComment);

        //when
        commentService.remove(parentComment.getId()); //부모댓글 삭제

        //then
        //부모댓글 - existState = false, 자식댓글 - existState = true
        Assertions.assertThat(parentComment.getExistState()).isFalse();
        Assertions.assertThat(childComment.getExistState()).isTrue();
    }

    @Test
    void 댓글리스트_조회() throws Exception{
        //given
        Meeting meeting = createMeeting();

        //방장 프로필 사진 생성
        MemberImg masterImg = MemberImg.builder()
                .member(meeting.getMember())
                .folderPath("vine/2021/09/21")
                .storeFileName("123Rfl_file1.jpeg")
                .build();
        memberImgRepository.save(masterImg);

        //댓글 작성자 생성, 프로필 사진 생성
        Member writer = Member.builder()
                .name("writer")
                .email("writer@kookmin.ac.kr")
                .nickname("writer")
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(writer);

        MemberImg writerImg = MemberImg.builder()
                .member(writer)
                .folderPath("vine/2021/09/21")
                .storeFileName("123Rfl_file1.jpeg")
                .build();
        memberImgRepository.save(writerImg);

        //댓글 생성
        /**
         * parentCommentA -> childComment 1, childComment 3
         * parentCommentB -> childComment 2
         */
        //parentComment 생성
        Comment parentCommentA = createComment(meeting.getMember()); //parentCommentA 방장 댓글
        parentCommentA.setMeeting(meeting);
        commentRepository.save(parentCommentA);

        Comment parentCommentB = createComment(writer); //parentCommentB 일반회원 댓글
        parentCommentB.setMeeting(meeting);
        commentRepository.save(parentCommentB);

        //childComment 생성
        Comment childComment1 = createComment(writer); //parentCommentA의 일반회원 대댓글
        childComment1.setMeeting(meeting);
        childComment1.setParent(parentCommentA);
        commentRepository.save(childComment1);

        Comment childComment2 = createComment(meeting.getMember()); //parentCommentB의 방장 대댓글
        childComment2.setMeeting(meeting);
        childComment2.setParent(parentCommentB);
        commentRepository.save(childComment2);

        Comment childComment3 = createComment(meeting.getMember()); //parentCommentA의 방장 대댓글
        childComment3.setMeeting(meeting);
        childComment3.setParent(parentCommentA);
        commentRepository.save(childComment3);


        //when
        CommentReadDTO result = commentService.getCommentList(meeting.getId());

        //then
        System.out.println(result.getCommentCount());
        for (CommentResDTO commentResDTO : result.getCommentResDTOList()){
            System.out.println(commentResDTO);
        }

        Assertions.assertThat(result.getCommentCount()).isEqualTo(5);
        Assertions.assertThat(result.getCommentResDTOList().size()).isEqualTo(2);

        //첫번째 댓글 - childList
        CommentResDTO first = result.getCommentResDTOList().get(0);
        Assertions.assertThat(first.getChildList().size()).isEqualTo(2);

        //두번째 댓글 - childList
        CommentResDTO second = result.getCommentResDTOList().get(1);
        Assertions.assertThat(second.getChildList().size()).isEqualTo(1);
    }


    private Comment createComment(Member member) {

        Comment comment = Comment.builder()
                .member(member)
                .content("기대돼요!")
                .open(true)
                .build();

        return comment;
    }

    private Meeting createMeeting() {
        Member member = createMember();

        Meeting meeting = Meeting.builder()
                .category(Category.SPORTS)
                .member(member)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2022, 06, 05, 00, 00))
                .reqDeadline(LocalDateTime.of(2022, 06, 04, 00, 00))
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
}
