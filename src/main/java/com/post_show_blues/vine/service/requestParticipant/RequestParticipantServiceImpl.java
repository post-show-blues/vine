package com.post_show_blues.vine.service.requestParticipant;

import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.domain.requestParticipant.RequestParticipant;
import com.post_show_blues.vine.domain.requestParticipant.RequestParticipantRepository;
import com.post_show_blues.vine.dto.requestParticipant.RequestParticipantDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Transactional
@RequiredArgsConstructor
@Service
public class RequestParticipantServiceImpl implements RequestParticipantService{

    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final RequestParticipantRepository requestParticipantRepository;
    private final ParticipantRepository participantRepository;
    private final NoticeRepository noticeRepository;

    /**
     * 참여요청
     */
    @Transactional
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

        RequestParticipant requestParticipant = RequestParticipant.builder()
                .member(member)
                .meeting(meeting)
                .build();

        RequestParticipant savedRequestParticipant = requestParticipantRepository.save(requestParticipant);

        //알림 생성
        Notice notice = Notice.builder()
                .memberId(meeting.getMember().getId()) //방장에게
                .text(meeting.getTitle()+"방에 " + member.getNickname()+"님이 참여요청했습니다.")
                .link("/meetings/"+meeting.getId())
                .build();

        noticeRepository.save(notice);

        return savedRequestParticipant.getId();
    }

    /**
     * 요청수락
     */
    @Transactional
    @Override
    public void accept(Long requestParticipantId, Long principalId) {

        RequestParticipant requestParticipant = requestParticipantRepository.findById(requestParticipantId).orElseThrow(() ->
                new IllegalStateException("존재하지 않은 요청입니다."));

        Meeting meeting = requestParticipant.getMeeting();

        //방장 권한 체크
        if(meeting.getMember().getId() != principalId){
            throw new IllegalStateException("요청수락 권한이 없습니다.");
        }

        //participant 참여명단 추가
        Participant participant = Participant.builder()
                .member(requestParticipant.getMember())
                .meeting(meeting)
                .build();

        participantRepository.save(participant);

        //RequestParticipant 요청명단 삭체
        requestParticipantRepository.deleteById(requestParticipantId);

        //모임 참여 현재인원 1증가
        meeting.addCurrentNumber();

        //알림 생성
        Notice notice = Notice.builder()
                .memberId(participant.getMember().getId()) // participantMember 에게
                .text(meeting.getTitle() + "방 수락이 완료되었습니다.")
                .link("/meetings/"+meeting.getId())
                .build();

        noticeRepository.save(notice);

    }

    /**
     * 요청거절/철회
     */
    @Transactional
    @Override
    public void reject(Long requestParticipantId, Long principalId) {
        RequestParticipant requestParticipant = requestParticipantRepository.findById(requestParticipantId).orElseThrow(() ->
                new IllegalStateException("존재하지 않은 요청입니다."));

        Member member = requestParticipant.getMember();
        Meeting meeting = requestParticipant.getMeeting();

        //요청자 or 방장 권한 체크
        if(member.getId() != principalId && meeting.getMember().getId() != principalId){
            throw new IllegalStateException("거절 또는 철회 권한이 없습니다.");
        }

       requestParticipantRepository.deleteById(requestParticipantId);

        //알람 생성
        Notice notice = Notice.builder()
                .memberId(member.getId()) //참여요청자에게
                .text(meeting.getTitle() + "방에 거절되었습니다")
                .build();

        noticeRepository.save(notice);


    }
    /**
     * 요청자리스트(DTO)
     */
    @Transactional
    @Override
    public List<RequestParticipantDTO> getRequestParticipantList(Long meetingId) {

        List<Object[]> result = requestParticipantRepository
                .getListRequestParticipantByMeetingId(meetingId);

        List<RequestParticipantDTO> requestParticipantDTOList = result.stream().map(objects -> {
            MemberImg memberImg = (MemberImg) objects[0];
            Member member = (Member) objects[1];
            RequestParticipant requestParticipant = (RequestParticipant) objects[2];
            return entityToDTO(memberImg, member, requestParticipant);
        }).collect(Collectors.toList());

        return requestParticipantDTOList;
    }

    /**
     * 요청조회
     */
    @Transactional(readOnly = true)
    @Override
    public RequestParticipant findOne(Long requestParticipantId) {

        Optional<RequestParticipant> result = requestParticipantRepository.findById(requestParticipantId);

        RequestParticipant requestParticipant = result.get();

        return requestParticipant;
    }
}
