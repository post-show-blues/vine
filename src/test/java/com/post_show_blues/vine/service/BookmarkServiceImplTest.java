package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.bookmark.Bookmark;
import com.post_show_blues.vine.domain.bookmark.BookmarkRepository;
import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.service.bookmark.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest
public class BookmarkServiceImplTest {

    @Autowired
    BookmarkService bookmarkService;
    @Autowired
    BookmarkRepository bookmarkRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MeetingRepository meetingRepository;

    @Test
    void 북마크생성() throws Exception {
        //given
        //모임 생성
        Member master = createMember("master@nate.com", "master");
        Meeting meeting = createMeeting(master);

        //회원 생성
        Member member = createMember("memberA@nate.com", "memberA");

        //when
        Bookmark bookmark = bookmarkService.bookmark(meeting.getId(), member.getId());

        //then
        Assertions.assertThat(bookmark.getId()).isNotNull();
        Assertions.assertThat(bookmark.getMeeting().getId()).isEqualTo(meeting.getId());
        Assertions.assertThat(bookmark.getMember().getId()).isEqualTo(member.getId());
        Assertions.assertThat(meeting.getBookmarkList().size()).isEqualTo(1);
    }

    private Meeting createMeeting(Member master) {

        Meeting meeting = Meeting.builder()
                .category(Category.SPORTS)
                .member(master)
                .title("MeetingA")
                .text("meet")
                .place("A")
                .meetDate(LocalDateTime.of(2021, 06, 05, 00, 00))
                .reqDeadline(LocalDateTime.of(2021, 06, 04, 00, 00))
                .dDay(Duration.between(LocalDate.now().atStartOfDay(),
                        LocalDateTime.of(2021, 06, 05, 00, 00)
                                .toLocalDate().atStartOfDay()).toDays())
                .maxNumber(4)
                .currentNumber(3)
                .build();

        meetingRepository.save(meeting);

        return meeting;
    }

    private Member createMember(String email, String nickname) {
        Member member = Member.builder()
                .name("member")
                .email(email)
                .nickname(nickname)
                .password("1111")
                .phone("010-0000-0000")
                .university("국민대학교")
                .build();

        memberRepository.save(member);

        return member;

    }
}
