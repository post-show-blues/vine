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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/members/{member-id}/notices")
@Controller
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping //알림목록
    public String noticeList(@PathVariable("member-id") Long memberId,
                             PageRequestDTO pageRequestDTO,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             Model model){

        PageResultDTO<NoticeDTO, Notice> noticeDTOList = noticeService.getNoticeList(pageRequestDTO, memberId);

        model.addAttribute("noticeDTOList", noticeDTOList);
        return "";

    }

    @PostMapping("/{notice-id}/delete") //알림삭제
    public String deleteNotice(@PathVariable("member-id") Long memberId,
                               @PathVariable("notice-id") Long noticeId){


        return "";
    }
}
