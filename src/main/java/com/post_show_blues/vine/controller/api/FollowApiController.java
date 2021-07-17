package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//데이터만 리턴하는 컨트롤러
@RequiredArgsConstructor
@RestController
public class FollowApiController {

    private final FollowService followService;

    @PostMapping("/api/subscribe/{toMemberId}")
    public ResponseEntity<?> follow(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable long toMemberId){
        followService.isFollow(principalDetails.getMember().getId(), toMemberId);
        return new ResponseEntity<>(new CMRespDto<>(1, "구독하기 성공", null), HttpStatus.OK);
    }
    @DeleteMapping("/api/subscribe/{toMemberId}")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable long toMemberId){
        followService.isUnFollow(principalDetails.getMember().getId(), toMemberId);
        return new ResponseEntity<>(new CMRespDto<>(1, "구독취소하기 성공", null), HttpStatus.OK);
    }

    @GetMapping("member/follow/{id}")
    public String followMember(@PathVariable Long id, Model model){
        return "";
    }

    @GetMapping("member/follower/{id}")
    public String followerMember(@PathVariable Long id, Model model){
        return "";
    }
}
