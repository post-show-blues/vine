package com.post_show_blues.vine.domain.notice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NoticeRepositoryTest {

    @Autowired NoticeRepository noticeRepository;

    @Test
    void testGetListPage() throws Exception{
        //given
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id").descending());

        Notice notice1 = createNotice();
        Notice notice2 = createNotice();
        Notice notice3 = createNotice();
        Notice notice4 = createNotice();
        Notice notice5 = createNotice();
        Notice notice6 = createNotice();


        //when
        Page<Notice> result = noticeRepository.getListPage(pageRequest, 1L);


        //then
        Assertions.assertThat(result.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(6);
        Assertions.assertThat(result.getNumber()).isEqualTo(0);
        Assertions.assertThat(result.getSize()).isEqualTo(5);
        Assertions.assertThat(result.getSort()).isEqualTo(Sort.by("id").descending());


    }

    private Notice createNotice() {

        Notice notice = Notice.builder()
                .memberId(1L)
                .text("형우님이 팔로우 신청을 했습니다.")
                .link("/member/guddn")
                .readState(false)
                .build();

        noticeRepository.save(notice);

        return notice;
    }


}