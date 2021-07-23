package com.post_show_blues.vine.controller.api;


import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.member.MemberUpdateDto;
import com.post_show_blues.vine.dto.memberImg.MemberImgUploadDto;
import com.post_show_blues.vine.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;

    @PutMapping("/profile/{id}")
    public CMRespDto<?> profileUpdate(
            @PathVariable Long id,
            MemberUpdateDto memberUpdateDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails){ //text 수정 가능
        Member memberEntity = memberService.memberUpdate(id, memberUpdateDto.toEntity());

        //세션정보 바꿔주기
        principalDetails.setMember(memberEntity);
        return new CMRespDto<>(1, "회원수정완료", memberEntity);
    }

    @PutMapping("/img/{id}")
    public CMRespDto<?> imgUpdate(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            MemberImgUploadDto memberImgUploadDto){ //text 수정 가능

        Optional<MemberImgUploadDto> imgDto = Optional.ofNullable(memberImgUploadDto);
        memberService.memberImgUpdate(principalDetails.getMember(), imgDto);
        //세션정보 바꿔주기
        return new CMRespDto<>(1, "회원사진 업데이트 완료", null);
    }

    //사람 검색
    @GetMapping("/find")
    public CMRespDto<?> findMember(String keyword){
        List<Member> members = memberService.findMember(keyword);
        return new CMRespDto<>(1, "회원 검색 완료", members);
    }
}
