package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.dto.MemberImgDTO;
import com.post_show_blues.vine.dto.ParticipantDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipantServiceImpl implements ParticipantService{

    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;
    private final NoticeRepository noticeRepository;


    /**
     * 참여요청
     */
    @Override
    public Long request(Long meetingId, Long memberId) {

        Optional<Meeting> findMeeting = meetingRepository.findById(meetingId);
        Meeting meeting = findMeeting.get();

        Optional<Member> findMember = memberRepository.findById(memberId);
        Member member = findMember.get();


        //요청마감일 체크
        LocalDateTime reqDeadline = meeting.getReqDeadline();

        //TODO 테스트 코드 실행시 주석처리 함
        /*
        try{
            Date deadlineDate = dateFormat.parse(reqDeadline);
            Date now = new Date();
            if(deadlineDate.before(now)){
                throw new IllegalStateException("참여 가능일이 지났습니다.");
            }
        }catch (ParseException e){
            e.printStackTrace();
            throw new IllegalStateException("형식에 맞게 입력해주세요.");
        }
        catch (Exception e){
            e.printStackTrace();
            throw new IllegalStateException("참여 가능일이 지났습니다.");
        }
        */

        //참여인원 체크
        int maxNumber = meeting.getMaxNumber();
        int currentNumber = meeting.getCurrentNumber();

        if(maxNumber <= currentNumber){
            throw new IllegalStateException("참여인원 초과입니다.");
        }

        Participant participant = Participant.builder()
                .member(member)
                .meeting(meeting)
                .build();

        participantRepository.save(participant);

        //TODO 2021.06.04.-요청시 방장에게 수락요청 알림-hyeongwoo
        /*Long masterId = meeting.getMember().getId();

        Notice notice = Notice.builder()
                .memberId(masterId)
                .text(member.getNickname()+"님이 " + meeting.getTitle()+"방에 참여 요청했습니다.")
                .link()
                .build();

        noticeRepository.save(notice);*/

        return participant.getId();
    }

    /**
     * 요청수락
     */
    @Override
    public void accept(Long participantId) {

        Optional<Participant> result = participantRepository.findById(participantId);
        Participant participant = result.get();

        Meeting meeting = participant.getMeeting();

        //모임 참여 현재인원 1증가
        meeting.addCurrentNumber();

        //참여 request변수 true로 변경
        participant.changeReq(true);

        //TODO 2021.06.04.-수락시 요청한 회원에게 수락완료 알림-hyeongwoo
        /*Long memberId = participant.getMember().getId();

        Notice notice = Notice.builder()
                .id(memberId)
                .text(meeting.getTitle() + "방 수락이 완료되었습니다.")
                .link()
                .build();

        noticeRepository.save(notice);*/

    }

    /**
     * 요청거절
     */
    @Override
    public void reject(Long participantId) {
        Optional<Participant> result = participantRepository.findById(participantId);
        Participant participant = result.get();

        Member member = participant.getMember();
        Meeting meeting = participant.getMeeting();

        participantRepository.deleteById(participantId);

        //TODO 2021.06.04.-거절 당한 회원에게 알림-hyeongwoo
        /*
        Notice notice = Notice.builder()
                .memberId(member.getId())
                .text(meeting.getTitle() + "방에 거절되었습니다")
                .link()
                .build();

        noticeRepository.save(notice);
        */

    }

    /**
     * 추방_나가기
     */
    @Override
    public void remove(Long participantId, Long memberId) {
        Optional<Participant> result = participantRepository.findById(participantId);
        Participant participant = result.get();

        Meeting meeting = participant.getMeeting();

        meeting.removeCurrentNumber();

        participantRepository.deleteById(participantId);

        //TODO 2021.06.04
        // 1.추방 -> 회원에게 알림
        // 2. 나가기 -> 방장에게 알림
        // -hyeongwoo
        /*
        //알림
        //방장에게 알림(나감)
        if(meeting.getMember().getId() == memberId){
            Notice masterNotice = Notice.builder()
                    .memberId(memberId)
                    .text(meeting.getTitle() + "방 인원이 나갔습니다.")
                    .link()
                    .build();
            noticeRepository.save(masterNotice);
        }
        //회원에게 알림(추방)
        else{
            Notice memberNotice = Notice.builder()
                    .memberId(participant.getMember().getId())
                    .text(meeting.getTitle() + "방에 ~~했습니다.")
                    .link()
                    .build();
            noticeRepository.save(memberNotice);
        }
         */
    }

    /**
     * 참여자리스트(DTO)
     */
    @Override
    @Transactional(readOnly = true)
    public List<ParticipantDTO> getParticipantList(Long meetingId) {

        List<Object[]> result = participantRepository.getListParticipantByMeetingId(meetingId);

        List<ParticipantDTO> participantDTOList = result.stream().map(objects -> {
            MemberImg memberImg = (MemberImg) objects[0];
            Member member = (Member) objects[1];
            Participant participant = (Participant) objects[2];
            return entityToDTO(memberImg, member, participant);
        }).collect(Collectors.toList());

        return participantDTOList;
    }

    /**
     * 참여조회
     */
    @Override
    @Transactional(readOnly = true)
    public Participant findOne(Long id) {
        Optional<Participant> result = participantRepository.findById(id);

        Participant participant = result.get();

        return participant;
    }
}
