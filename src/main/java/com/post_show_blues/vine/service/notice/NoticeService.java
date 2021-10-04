package com.post_show_blues.vine.service.notice;

import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.dto.notice.NoticeDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;

public interface NoticeService {

    void dDayNotice();

    PageResultDTO<NoticeDTO, Notice> getNoticeList(PageRequestDTO requestDTO, Long memberId);

    void changeRead(Long noticeId);

    int unReadCount(Long memberId);

    void remove(Long memberId);

    default NoticeDTO entityToDTO(Notice notice){

        NoticeDTO noticeDTO = NoticeDTO.builder()
                .noticeId(notice.getId())
                .memberId(notice.getMemberId())
                .text(notice.getText())
                .link(notice.getLink())
                .state(notice.getReadState())
                .build();

        return noticeDTO;
    }
}
