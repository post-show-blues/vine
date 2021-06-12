package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.dto.NoticeDTO;
import com.post_show_blues.vine.dto.PageResultDTO;
import org.springframework.data.domain.Page;

public interface NoticeService {

    void D_dayNotice();

    PageResultDTO<NoticeDTO, Notice> getNoticeList();

    default NoticeDTO entityToDTO(Notice notice){

        NoticeDTO noticeDTO = NoticeDTO.builder()
                .noticeId(notice.getId())
                .memberId(notice.getMemberId())
                .text(notice.getText())
                .link(notice.getLink())
                .state(notice.getState())
                .build();

        return noticeDTO;
    }
}
