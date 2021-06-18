package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.follow.FollowRepository;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.dto.NoticeResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    /**
     * 팔로우
     */
    @Transactional
    public void isFollow(long fromMemberId, long toMemberId){
        followRepository.rFollow(fromMemberId, toMemberId);
        noticeSave(fromMemberId, toMemberId);
    }

    /**
     * 언팔로우
     */
    @Transactional
    public void isUnFollow(long fromMemberId, long toMemberId){
        followRepository.rUnFollow(fromMemberId, toMemberId);
    }

    private void noticeSave(long fromMemberId, long toMemberId){
        String fromMemberNickname = memberRepository.findById(fromMemberId).get().getNickname();

        NoticeResultDTO noticeResDTO = NoticeResultDTO.builder()
                .memberId(toMemberId)
                .text(fromMemberNickname + "님이 회원님을 팔로잉 했습니다.")
                .link("/member/"+fromMemberId)
                .build();

        noticeRepository.save(noticeResDTO.toEntity());
    }
}
