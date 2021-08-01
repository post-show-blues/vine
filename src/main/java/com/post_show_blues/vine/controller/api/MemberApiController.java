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
public class MemberApiController {

    private final MemberService memberService;

    @PutMapping("/profile")
    public CMRespDto<?> profileUpdate(
            MemberUpdateDto memberUpdateDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) { //text 수정 가능
        Member memberEntity = memberService.memberUpdate(principalDetails.getId(), memberUpdateDto.toEntity());

        //세션정보 바꿔주기
        principalDetails.setMember(memberEntity);
        return new CMRespDto<>(1, "회원수정완료", memberEntity);
    }

    @PutMapping("/img")
    public CMRespDto<?> imgUpdate(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            MemberImgUploadDto memberImgUploadDto) {

        Optional<MemberImgUploadDto> imgDto = Optional.ofNullable(memberImgUploadDto);
        memberService.memberImgUpdate(principalDetails.getMember(), imgDto);
        //세션정보 바꿔주기
        return new CMRespDto<>(1, "회원사진 업데이트 완료", null);
    }

    //사람 검색
    @GetMapping("/find/{keyword}")
    public CMRespDto<?> findMember(@PathVariable String keyword) {
        List<Member> members = memberService.findMember(keyword);
        return new CMRespDto<>(1, "회원 검색 완료", members);
    }

    //프로필 조회
    @GetMapping("/")
    public CMRespDto<?> profile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        //세션에서 멤버 찾아옴
        System.out.println("principle"+principalDetails);
        Member member = principalDetails.getMember();
        log.info("세션정보 : " + member);
        return new CMRespDto<>(1, "회원 프로필 조회", member);
    }

    //TODO : 다른사람이 내 프로필 조회

}