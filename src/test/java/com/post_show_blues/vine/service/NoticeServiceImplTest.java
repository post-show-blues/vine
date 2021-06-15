package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.dto.NoticeDTO;
import com.post_show_blues.vine.dto.PageRequestDTO;
import com.post_show_blues.vine.dto.PageResultDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class NoticeServiceImplTest {

    @Autowired NoticeService noticeService;
    @Autowired NoticeRepository noticeRepository;

    @Test
    void 알림_리스트() throws Exception{
        //given
        Notice notice1 = createNotice();
        Notice notice2 = createNotice();
        Notice notice3 = createNotice();
        Notice notice4 = createNotice();
        Notice notice5 = createNotice();
        Notice notice6 = createNotice();
        Notice notice7 = createNotice();
        Notice notice8 = createNotice();
        Notice notice9 = createNotice();
        Notice notice10 = createNotice();
        Notice notice11 = createNotice();


        PageRequestDTO requestDTO = new PageRequestDTO();

        //when
        PageResultDTO<NoticeDTO, Notice> result = noticeService.getNoticeList(requestDTO, 1L);

        //then
        Assertions.assertThat(result.getTotalPage()).isEqualTo(2);
        Assertions.assertThat(result.getSize()).isEqualTo(10);
        Assertions.assertThat(result.getStart()).isEqualTo(1);
        Assertions.assertThat(result.getDtoList().size()).isEqualTo(10);
    }

    @Test
    void 읽음표시() throws Exception{
        //given
        Notice notice = createNotice();

        //when
        noticeService.changeRead(notice.getId());

        //then
        Optional<Notice> result = noticeRepository.findById(notice.getId());
        Notice notice1 = result.get();
        Assertions.assertThat(notice1.getState()).isTrue();
    }

    @Test
    void 안읽음_개수() throws Exception{
        //given
        Notice notice1 = createNotice();
        Notice notice2 = createNotice();
        Notice notice3 = createNotice();

        notice3.changeState();

        //when
        Assertions.assertThat(noticeRepository.getUnreadCount(1L)).isEqualTo(2);

    }

    private Notice createNotice() {

        Notice notice = Notice.builder()
                .memberId(1L)
                .text("형우님이 팔로우 신청을 했습니다.")
                .link("/member/guddn")
                .state(false)
                .build();

        noticeRepository.save(notice);

        return notice;

    }

}