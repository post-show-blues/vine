package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.dto.NoticeDTO;
import com.post_show_blues.vine.dto.PageResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;
    private final MeetingRepository meetingRepository;

    @Override
    @Scheduled(cron = "0 29 18 * * ?")
    //TODO 2021.06.30.
    // -D-day에 해당하는 모임 방장, 참여자들에게 알림 생성
    // -hyeongwoo
    public void D_dayNotice() {
        System.out.println("guddn");
    }

    @Override
    public PageResultDTO<NoticeDTO, Notice> getNoticeList() {




        return null;
    }
}
