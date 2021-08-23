package com.post_show_blues.vine.controller.api;


import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.member.MemberUpdateDto;
import com.post_show_blues.vine.service.member.MemberUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberUpdateApiController {

    private final MemberUpdateService memberUpdateService;

    @PutMapping("/profile/edit")
    public CMRespDto<?> profileUpdate(
            MemberUpdateDto memberUpdateDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) { //text 수정 가능
        log.info("principle : " + principalDetails);

        if (principalDetails != null) {
            Member memberEntity = memberUpdateService.memberUpdate(principalDetails.getId(), memberUpdateDto.toEntity());
            //세션정보 바꿔주기
            principalDetails.setMember(memberEntity);
            return new CMRespDto<>(1, "회원수정완료", memberEntity);
        }
        throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다");

    }

    @PutMapping("/img/edit")
    public CMRespDto<?> imgUpdate(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            MultipartFile img) throws IOException {
        log.info("principle : " + principalDetails);

        if (principalDetails != null) {

            Optional<MultipartFile> imgDto = Optional.ofNullable(img);
            memberUpdateService.memberImgUpdate(principalDetails.getMember(), imgDto);
            //세션정보 바꿔주기
            return new CMRespDto<>(1, "회원사진 업데이트 완료", null);
        }
        throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다");

    }


}