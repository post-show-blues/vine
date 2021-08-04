package com.post_show_blues.vine.controller.api;


import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.member.MemberUpdateDto;
import com.post_show_blues.vine.dto.memberImg.MemberImgUploadDto;
import com.post_show_blues.vine.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberUpdateApiController {

    private final MemberService memberService;

    @PutMapping("/profile/edit")
    public CMRespDto<?> profileUpdate(
            MemberUpdateDto memberUpdateDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) { //text 수정 가능
        log.info("principle : " + principalDetails);

        if (principalDetails != null) {
            Member memberEntity = memberService.memberUpdate(principalDetails.getId(), memberUpdateDto.toEntity());
            //세션정보 바꿔주기
            principalDetails.setMember(memberEntity);
            return new CMRespDto<>(1, "회원수정완료", memberEntity);
        }
        throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다");

    }

    @PutMapping("/img")
    public CMRespDto<?> imgUpdate(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            MemberImgUploadDto memberImgUploadDto) {
        log.info("principle : " + principalDetails);

        if (principalDetails != null) {

            Optional<MemberImgUploadDto> imgDto = Optional.ofNullable(memberImgUploadDto);
            memberService.memberImgUpdate(principalDetails.getMember(), imgDto);
            //세션정보 바꿔주기
            return new CMRespDto<>(1, "회원사진 업데이트 완료", null);
        }
        throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다");

    }


}