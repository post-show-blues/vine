package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.member.FindMemberResultDTO;
import com.post_show_blues.vine.dto.member.MemberProfileDTO;
import com.post_show_blues.vine.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberApiController {
    private final MemberService memberService;

    //TODO : 필요 정보만 조회
    //프로필 조회
    @GetMapping("/profile")
    public CMRespDto<?> Myprofile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        //세션에서 멤버 찾아옴
        log.info("principle : " + principalDetails);

        if (principalDetails != null) {
            Member member = principalDetails.getMember();
            log.info("세션정보 : " + member);
            return new CMRespDto<>(1, "내 프로필 조회", member);
        }
        throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다");
    }

//    TODO : 다른사람의 프로필 조회
//    로그인 된 사용자 -> 팔로우 여부 표시
//    로그인 안 된 사용자 -> 팔로우 여부 표시 x

    @GetMapping("/profile/{id}")
    public CMRespDto<?> OthersProfile(@PathVariable Long id) {
        MemberProfileDTO memberProfileDTO = memberService.findMemberProfile(id);
        return new CMRespDto<>(1, "다른 회원 프로필 조회", memberProfileDTO);
    }

    @GetMapping("/find/{keyword}")
    public CMRespDto<?> findMember(@PathVariable String keyword) {
        List<FindMemberResultDTO> findMemberResultDTO = memberService.findMember(keyword);
        return new CMRespDto<>(1, "회원 검색 완료", findMemberResultDTO);
    }
}
