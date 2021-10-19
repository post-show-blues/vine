package com.post_show_blues.vine.domain.meeting;

import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.domain.comment.CommentRepository;
import com.post_show_blues.vine.domain.heart.Heart;
import com.post_show_blues.vine.domain.heart.HeartRepository;
import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import com.post_show_blues.vine.domain.meetingimg.MeetingImgRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
@Transactional
class MeetingRepositoryTest {

    @Autowired MeetingRepository meetingRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberImgRepository memberImgRepository;
    @Autowired ParticipantRepository participantRepository;
    @Autowired MeetingImgRepository meetingImgRepository;
    @Autowired HeartRepository heartRepository;
    @Autowired CommentRepository commentRepository;


    @Test
    void testGetMeetingWithAll() throws Exception {
        //given
        Meeting meeting = createMeeting();

        //모임 사진 2개 생성
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

        //방장 프로필 사진 생성
        Member master = meeting.getMember();

        MemberImg masterImg = MemberImg.builder()
                .member(master)
                .storeFileName(UUID.randomUUID().toString() + "_masterImg1")
                .folderPath("/hyeongwoo1")
                .build();

        memberImgRepository.save(masterImg);

        //새로운 유저 생성
        Member memberUser = Member.builder()
                .email("memberUser@kookmin.ac.kr")
                .nickname("memberUserNickname")
                .password("1111")
                .build();
        memberRepository.save(memberUser);

        //댓글 생성
        Comment parentComment = createComment(memberUser); //새로운 유저가 댓글 생성
        parentComment.setMeeting(meeting);
        commentRepository.save(parentComment);

        //대댓글 생성
        Comment childComment = createComment(meeting.getMember()); //방장이 대댓글 생성
        childComment.setParent(parentComment);
        childComment.setMeeting(meeting);
        commentRepository.save(childComment);

        //하트 생성 - 2개
        Heart heartA = createHeart(memberUser);
        heartA.setMeeting(meeting);
        heartRepository.save(heartA);

        Heart heartB = createHeart(meeting.getMember());
        heartB.setMeeting(meeting);
        heartRepository.save(heartB);


        //when
        List<Object[]> objects = meetingRepository.getMeetingWithAll(meeting.getId());

        //then
        for (Object[] object : objects){
            System.out.println(Arrays.toString(object));
        }

        Assertions.assertThat(objects.size()).isEqualTo(2);
    }

    private Comment createComment(Member member) {

        Comment comment = Comment.builder()
                .member(member)
                .content("기대돼요!")
                .open(true)
                .build();

        return comment;
    }

    private Heart createHeart(Member member) {
        Heart heart = Heart.builder()
                .member(member)
                .build();

        return heart;
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

    private Meeting createMeeting() {
        Member member = createMember();

        Meeting meeting = Meeting.builder()
                .category(Category.SPORTS)
                .member(member)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2021,06,05,12,00))
                .reqDeadline(LocalDateTime.of(2021,06,04,14,00))
                .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                        LocalDateTime.of(2021,06,05,00,00)
                                .toLocalDate().atStartOfDay()).toDays())
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meeting);

        return meeting;

    }
}