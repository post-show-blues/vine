package com.post_show_blues.vine.controller;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.dto.NoticeDTO;
import com.post_show_blues.vine.dto.PageRequestDTO;
import com.post_show_blues.vine.dto.PageResultDTO;
import com.post_show_blues.vine.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/notice")
@Controller
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/{id}")
    public String listNotice(@PathVariable("id") Long memberId,
                             PageRequestDTO pageRequestDTO,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             Model model){

        PageResultDTO<NoticeDTO, Notice> noticeDTOList = noticeService.getNoticeList(pageRequestDTO, memberId);

        model.addAttribute("noticeDTOList", noticeDTOList);
        return "";

    }
}
