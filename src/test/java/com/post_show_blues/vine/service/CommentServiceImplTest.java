package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.domain.comment.CommentRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.dto.comment.CommentDTO;
import com.post_show_blues.vine.service.comment.CommentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
@SpringBootTest
public class CommentServiceImplTest {

    @Autowired CommentService commentService;
    @Autowired MemberRepository memberRepository;
    @Autowired MeetingRepository meetingRepository;
    @Autowired CommentRepository commentRepository;


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
