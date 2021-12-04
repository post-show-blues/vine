package com.post_show_blues.vine.service.notice;

import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.dto.notice.NoticeDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.service.meeting.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;
    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;
    private final MeetingService meetingService;


    /**
     * D-day 모임 참여자들에게 알림 생성
     */
    @Scheduled(cron = "0 00 00 * * ?")
    @Transactional
    @Override
    public void dDayNotice() {
        log.info("----------D-day 모임 참여자들에게 알림 생성 시작---------");
        //dDay -1 갱신
        meetingService.updatedDay();


        //dDay = 0 인 meetingList 조회
        List<Meeting> meetingList = meetingRepository.getDZeroMeeting();

        //알림 생성
        meetingList.forEach(meeting -> {

            //방장에게 활동일 알림
            Notice masterNotice = Notice.builder()
                    .memberId(meeting.getMember().getId())
                    .text("오늘은 "+meeting.getTitle() + "방의 활동일입니다.")
                    .link("/meetings/" + meeting.getId())
                    .build();

            noticeRepository.save(masterNotice);

            //참여자들에게 활동일 알림
            List<Participant> participantList = participantRepository.findByMeeting(meeting);

            participantList.forEach(participant -> {
                Notice participantNotice = Notice.builder()
                        .memberId(participant.getMember().getId())
                        .text("오늘은 "+meeting.getTitle() + "방의 활동입니다.")
                        .link("/meetings/" + meeting.getId())
                        .build();

                noticeRepository.save(participantNotice);
            });
        });

        log.info("----------D-day 모임 참여자들에게 알림 생성 종료---------");
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
