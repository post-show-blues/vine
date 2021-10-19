package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.member.MemberListDTO;
import com.post_show_blues.vine.dto.member.MemberProfileDTO;
import com.post_show_blues.vine.dto.member.MyProfileDTO;
import com.post_show_blues.vine.dto.member.ProfileMeetingDTO;
import com.post_show_blues.vine.service.member.MemberService;
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

    //내 프로필 조회
    @GetMapping("/profile")
    public CMRespDto<?> myProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        //세션에서 멤버 찾아옴
        log.info("principle : " + principalDetails);

        if (principalDetails != null) {
            Long id = principalDetails.getId();
            log.info("세션정보 : " + id);
            MyProfileDTO myProfileDTO = memberService.myProfile(id);
            return new CMRespDto<>(1, "내 프로필 조회", myProfileDTO);
        }
        throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다");
    }

    //다른 회원 프로필 조회
    @GetMapping("/profile/{id}")
    public CMRespDto<?> othersProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                      @PathVariable Long id) {
        Long myId = null;
        if (principalDetails != null) {
            myId = principalDetails.getId();
        }
        MemberProfileDTO memberProfileDTO = memberService.memberProfile(myId, id);
        return new CMRespDto<>(1, "다른 회원 프로필 조회", memberProfileDTO);
    }

    @GetMapping("/profile/meeting")
    public CMRespDto<?> myProfileMeeting(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails != null) {
            Long id = principalDetails.getId();
            log.info("세션정보 : " + id);
            List<ProfileMeetingDTO> myProfileDTO = memberService.profileMeeting(id);
            return new CMRespDto<>(1, "프로필 미팅 조회", myProfileDTO);
        }
        throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다");
    }

    @GetMapping("/profile/{id}/meeting")
    public CMRespDto<?> othersProfileMeeting(@PathVariable Long id) {
        List<ProfileMeetingDTO> participantMeeting = memberService.profileMeeting(id);
        return new CMRespDto<>(1, "참가 방 리스트 조회", participantMeeting);
    }

    @GetMapping("/find/{keyword}")
    public CMRespDto<?> memberList(@PathVariable String keyword) {
        System.out.println(keyword);
        List<MemberListDTO> findMemberResultDTO = memberService.memberList(keyword);
        return new CMRespDto<>(1, "회원 검색 완료", findMemberResultDTO);
    }
}
