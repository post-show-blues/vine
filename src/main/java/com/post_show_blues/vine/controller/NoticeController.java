package com.post_show_blues.vine.controller;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.dto.notice.NoticeDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@RequestMapping("/members/{member-id}/notices")
@Controller
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping //알림목록
    public String noticeList(@PathVariable("member-id") Long memberId,
                             @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             Model model){

        PageResultDTO<NoticeDTO, Notice> noticeDTOList = noticeService.getNoticeList(requestDTO, memberId);


        model.addAttribute("noticeDTOList", noticeDTOList);
        return "";

    }

    @PostMapping("/{notice-id}/delete") //알림삭제
    public String deleteNotice(@PathVariable("member-id") Long memberId,
                               @PathVariable("notice-id") Long noticeId,
                               RedirectAttributes redirectAttributes){

        noticeService.remove(noticeId);

        redirectAttributes.addAttribute("memberId", memberId);

        return "redirect:/members/{memberId}/notices";
    }
}
