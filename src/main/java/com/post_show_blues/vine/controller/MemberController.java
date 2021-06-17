package com.post_show_blues.vine.controller;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RequiredArgsConstructor
@Log4j2
@Controller
public class MemberController {

    //모델에 넘기는 것보다 front 단에서 세션에 접근하는 방법이 더 편하다
    @GetMapping("/member/{id}") //프로필 보기
    public String profile(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        //세션에서 멤버 찾아옴
        Member member = principalDetails.getMember();
        log.info("세션정보 : " + member);

        model.addAttribute("principal", member);

        return "";
    }

}
