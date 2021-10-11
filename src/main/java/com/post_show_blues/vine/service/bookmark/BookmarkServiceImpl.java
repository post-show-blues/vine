package com.post_show_blues.vine.service.bookmark;

import com.post_show_blues.vine.domain.bookmark.Bookmark;
import com.post_show_blues.vine.domain.bookmark.BookmarkRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BookmarkServiceImpl implements BookmarkService{

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;

    /**
     * 북마크 생성
     */
    @Transactional
    @Override
    public Bookmark bookmark(Long meetingId, Long principalId) {

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() ->
                new IllegalStateException("존재하지 않은 모임입니다."));

        Bookmark bookmark = Bookmark.builder()
                .member(Member.builder().id(principalId).build())
                .build();

        bookmark.setMeeting(meeting);

        bookmarkRepository.save(bookmark);

        return bookmark;
    }

    /**
     * 북마크 취소
     */
    @Transactional
    @Override
    public void cancelBookmark(Long meetingId, Long principalId) {

        bookmarkRepository.deleteBookmark(meetingId, principalId);
    }


}
