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

    //사람 검색
    @GetMapping("/find/{keyword}")
    public CMRespDto<?> findMember(@PathVariable String keyword) {
        List<Member> members = memberService.findMember(keyword);
        return new CMRespDto<>(1, "회원 검색 완료", members);
    }

    //TODO : 필요 정보만 조회
    //프로필 조회
    @GetMapping("/profile")
    public CMRespDto<?> Myprofile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        //세션에서 멤버 찾아옴
        log.info("principle : " + principalDetails);

        if (principalDetails != null) {
            Member member = principalDetails.getMember();
            log.info("세션정보 : " + member);
            return new CMRespDto<>(1, "회원 프로필 조회", member);
        }
        throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다");
    }

    //TODO : 다른사람이 내 프로필 조회
//    @GetMapping("/profile/{id}")
//    public CMRespDto<?> OthersProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
//                                      @PathVariable Long id) {
//        log.info("principle : " + principalDetails);
//        //로그인 돼 있는 사용자
//
//
//        //로그인 안 된 사용자
//
//    }
}