package com.post_show_blues.vine.service.follow;

import com.post_show_blues.vine.domain.follow.FollowRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.dto.member.MemberImgDTO;
import com.post_show_blues.vine.dto.follow.FollowMemberResultDTO;
import com.post_show_blues.vine.dto.follow.FollowerMemberResultDTO;
import com.post_show_blues.vine.dto.notice.NoticeResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Log4j2
@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    /**
     * 팔로우
     */
    @Transactional
    public void isFollow(long fromMemberId, long toMemberId) {
        followRepository.rFollow(fromMemberId, toMemberId);
        noticeSave(fromMemberId, toMemberId);
    }

    /**
     * 언팔로우
     */
    @Transactional
    public void isUnFollow(long fromMemberId, long toMemberId) {
        followRepository.rUnFollow(fromMemberId, toMemberId);
    }

    private void noticeSave(long fromMemberId, long toMemberId) {
        String fromMemberNickname = memberRepository.findById(fromMemberId).get().getNickname();

        NoticeResultDTO noticeResultDTO = NoticeResultDTO.builder()
                .memberId(toMemberId)
                .text(fromMemberNickname + "님이 회원님을 팔로잉 했습니다.")
                .link("/member/" + fromMemberId)
                .build();

        noticeRepository.save(noticeResultDTO.toEntity());
    }

    /**
     * 내가 팔로우하는 멤버 출력
     */
    public List<FollowMemberResultDTO> followMember(Long id) {
        List<Object[]> followMembers = followRepository.findFollowMembers(id);
        log.info("내가 팔로우하는 멤버 목록" + followMembers);
        if (followMembers == null) {
            return Collections.emptyList();
        }

        return followMembers.stream().map(fm -> {
            Member member = (Member) fm[0];
            MemberImg memberImg = (MemberImg) fm[1];

            if (memberImg != null) {
                MemberImgDTO memberImgDTO = MemberImgDTO.builder()
                        .folderPath(memberImg.getFolderPath())
                        .storeFileName(memberImg.getStoreFileName())
                        .build();

                return FollowMemberResultDTO.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .text(member.getText())
                        .memberImgDTO(memberImgDTO)
                        .build();
            } else {
                return FollowMemberResultDTO.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .text(member.getText())
                        .build();
            }
        }).collect(Collectors.toList());
    }


    /**
     * 나를 팔로우하는 멤버 출력
     */
    public List<FollowerMemberResultDTO> followerMember(Long id) {
        List<Object[]> followerMembers = followRepository.findFollowerMembers(id);

        if (followerMembers == null) {
            return Collections.emptyList();
        }

        return followerMembers.stream().map(fm -> {
            Member member = (Member) fm[0];
            MemberImg memberImg = (MemberImg) fm[1];
            Boolean isFollow = followRepository.existsByFromMemberIdAndToMemberId(member.getId(), id);

            if (memberImg != null) {
                MemberImgDTO memberImgDTO = MemberImgDTO.builder()
                        .folderPath(memberImg.getFolderPath())
                        .storeFileName(memberImg.getStoreFileName())
                        .build();

                return FollowerMemberResultDTO.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .text(member.getText())
                        .memberImgDTO(memberImgDTO)
                        .isFollow(isFollow)
                        .build();
            } else {
                return FollowerMemberResultDTO.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .text(member.getText())
                        .isFollow(isFollow)
                        .build();
            }

        }).collect(Collectors.toList());
    }
}
