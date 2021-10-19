package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.notice.NoticeDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RequiredArgsConstructor
@RequestMapping("/members/{memberId}/notices")
@Controller
public class NoticeApiController {

    private final NoticeService noticeService;

    @GetMapping //알림목록
    public ResponseEntity<?> noticeList(@PathVariable("memberId") Long memberId, PageRequestDTO requestDTO){

        PageResultDTO<NoticeDTO, Notice> noticeDTOList = noticeService.getNoticeList(requestDTO, memberId);

        return new ResponseEntity<>(new CMRespDto<>(1, "알림목록 불러오기 성공", noticeDTOList), HttpStatus.OK);

    }

    @DeleteMapping("/{noticeId}") //알림삭제
    public ResponseEntity<?> deleteNotice(@PathVariable("noticeId") Long noticeId){

        noticeService.remove(noticeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "알람삭제 상공", null), HttpStatus.OK);
    }
}
