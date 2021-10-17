package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.follow.FollowMemberResultDTO;
import com.post_show_blues.vine.dto.follow.FollowerMemberResultDTO;
import com.post_show_blues.vine.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FollowApiController {

    private final FollowService followService;

    @GetMapping("/follow/{toMemberId}")
    public ResponseEntity<?> follow(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable long toMemberId) {
        followService.isFollow(principalDetails.getMember().getId(), toMemberId);
        return new ResponseEntity<>(new CMRespDto<>(1, "팔로우 성공", null), HttpStatus.OK);
    }

    @DeleteMapping("/follow/{toMemberId}")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable long toMemberId) {
        followService.isUnFollow(principalDetails.getMember().getId(), toMemberId);
        return new ResponseEntity<>(new CMRespDto<>(1, "언팔로우 성공", null), HttpStatus.OK);
    }

    @DeleteMapping("/follower/{fromMemberId}")
    public ResponseEntity<?> followerUnfollow(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable long fromMemberId) {
        followService.isUnFollow(fromMemberId, principalDetails.getMember().getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "팔로워 끊기 성공", null), HttpStatus.OK);
    }

    @GetMapping("/follow")
    public ResponseEntity<?> followMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<FollowMemberResultDTO> followMembersResultDTO = followService.followMember(principalDetails.getMember().getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "팔로잉목록 불러오기 성공", followMembersResultDTO), HttpStatus.OK);
    }

    @GetMapping("/follower")
    public ResponseEntity<?> followerMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<FollowerMemberResultDTO> followerMembersResultDTO = followService.followerMember(principalDetails.getMember().getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "팔로워목록 불러오기 성공", followerMembersResultDTO), HttpStatus.OK);
    }
}
