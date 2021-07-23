package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.follow.FollowMemberResultDTO;
import com.post_show_blues.vine.dto.follow.FollowerMemberResultDTO;
import com.post_show_blues.vine.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//데이터만 리턴하는 컨트롤러
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FollowApiController {

    private final FollowService followService;

    @PostMapping("/subscribe/{toMemberId}")
    public ResponseEntity<?> follow(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable long toMemberId){
        followService.isFollow(principalDetails.getMember().getId(), toMemberId);
        return new ResponseEntity<>(new CMRespDto<>(1, "구독하기 성공", null), HttpStatus.OK);
    }
    @DeleteMapping("/subscribe/{toMemberId}")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable long toMemberId){
        followService.isUnFollow(principalDetails.getMember().getId(), toMemberId);
        return new ResponseEntity<>(new CMRespDto<>(1, "구독취소하기 성공", null), HttpStatus.OK);
    }

    @GetMapping("/member/follow")
    public ResponseEntity<?> followMember(@AuthenticationPrincipal PrincipalDetails principalDetails){
        List<FollowMemberResultDTO> followMembersResultDTO = followService.followMember(principalDetails.getMember().getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "팔로우목록 불러오기 성공", followMembersResultDTO), HttpStatus.OK);
    }

    @GetMapping("/member/follower")
    public ResponseEntity<?> followerMember(@AuthenticationPrincipal PrincipalDetails principalDetails){
        List<FollowerMemberResultDTO> followerMembersResultDTO = followService.followerMember(principalDetails.getMember().getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "팔로워목록 불러오기 성공", followerMembersResultDTO), HttpStatus.OK);
    }
}
