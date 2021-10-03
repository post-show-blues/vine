package com.post_show_blues.vine.service.bookmark;

import com.post_show_blues.vine.domain.bookmark.Bookmark;
import com.post_show_blues.vine.domain.bookmark.BookmarkRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
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

    /**
     * 북마크 생성
     */
    @Transactional
    @Override
    public Bookmark bookmark(Long meetingId, Long principalId) {

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new IllegalStateException("존재하는 않은 회원입니다."));

        Bookmark bookmark = Bookmark.builder()
                .meeting(Meeting.builder().id(meetingId).build())
                .build();

        bookmark.setMember(member);

        bookmarkRepository.save(bookmark);

        return bookmark;
    }
}
