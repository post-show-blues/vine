package com.post_show_blues.vine.controller.api;


import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.member.MemberUpdateResultDTO;
import com.post_show_blues.vine.dto.member.MyProfileUpdateRequestDTO;
import com.post_show_blues.vine.dto.member.MyProfileUpdateResultDTO;
import com.post_show_blues.vine.service.member.MemberUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberUpdateApiController {

    private final MemberUpdateService memberUpdateService;

    /**
     * 회원 정보 수정 페이지 조회
     */
    @GetMapping("/profile/edit")
    public CMRespDto<?> profileUpdate(
            @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException { //text 수정 가능

        log.info("principle : " + principalDetails);

        if (principalDetails != null) {
            MyProfileUpdateResultDTO myProfile = memberUpdateService.profile(principalDetails.getId());
            return new CMRespDto<>(1, "회원 수정 페이지 조회", myProfile);
        }
        throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다");
    }


    /**
     * 회원 정보 수정
     */
    @PutMapping("/profile/edit")
    public CMRespDto<?> profileUpdate(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @ModelAttribute MyProfileUpdateRequestDTO myProfileUpdateRequestDTO) throws IOException {

        log.info("principle : " + principalDetails);

        if (principalDetails != null) {
            Object[] object = memberUpdateService.memberUpdate(principalDetails.getId(), myProfileUpdateRequestDTO);
            Member member = (Member) object[0];
            MemberUpdateResultDTO memberUpdateResult = (MemberUpdateResultDTO) object[1];

            //세션정보 바꿔주기
            principalDetails.setMember(member);
            return new CMRespDto<>(1, "회원수정완료", memberUpdateResult);
        }
        throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다");
    }

}