package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.dto.notice.NoticeDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;
    private final MeetingRepository meetingRepository;


    /**
     * D-day 모임 참여자들에게 알림 생성
     */
    @Scheduled(cron = "0 29 18 * * ?")
    @Transactional
    @Override
    //TODO 2021.06.30.
    // -D-day에 해당하는 모임 방장, 참여자들에게 알림 생성
    // -hyeongwoo
    public void D_dayNotice() {
        System.out.println("guddn");
    }


    /**
     * 알림 DTO 리스트
     */
    @Transactional(readOnly = true)
    @Override
    public PageResultDTO<NoticeDTO, Notice> getNoticeList(PageRequestDTO requestDTO, Long memberId) {

        Pageable pageable = requestDTO.getPageable(Sort.by("id").descending());

        Page<Notice> result = noticeRepository.getListPage(pageable, memberId);

        Function<Notice, NoticeDTO> fn = (notice -> entityToDTO(notice));

        return new PageResultDTO<>(result, fn);
    }

    /**
     * 읽음으로 변경
     */
    @Transactional
    @Override
    public void changeRead(Long noticeId) {
        Optional<Notice> result = noticeRepository.findById(noticeId);

        if(result.isPresent()){
            Notice notice = result.get();
            notice.changeState();

        }

    }

    /**
     * 읽지않음 갯수
     */
    @Transactional(readOnly = true)
    @Override
    public int unReadCount(Long memberId) {
        int count = noticeRepository.getUnreadCount(memberId);

        return count;
    }


    /**
     * 알림삭제
     */
    @Transactional
    @Override
    public void remove(Long memberId) {

        noticeRepository.deleteById(memberId);

    }
}
