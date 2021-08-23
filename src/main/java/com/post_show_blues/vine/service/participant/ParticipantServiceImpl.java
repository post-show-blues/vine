package com.post_show_blues.vine.service.participant;

import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.notice.Notice;
import com.post_show_blues.vine.domain.notice.NoticeRepository;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.domain.participant.ParticipantRepository;
import com.post_show_blues.vine.dto.participant.ParticipantDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipantServiceImpl implements ParticipantService{

    private final ParticipantRepository participantRepository;
    private final NoticeRepository noticeRepository;

    /**
     * 추방_나가기
     */
    @Override
    public void remove(Long participantId, Long memberId) {
        Optional<Participant> result = participantRepository.findById(participantId);
        Participant participant = result.get();

        Meeting meeting = participant.getMeeting();

        meeting.removeCurrentNumber();


        /** 1.추방 -> 회원에게 알림
         *  2. 나가기 -> 방장에게 알림
         */
        //알림
        //회원에게 알림(추방)
        if(meeting.getMember().getId() == memberId){

            Notice memberNotice = Notice.builder()
                    .memberId(participant.getMember().getId()) //회원에게
                    .text(meeting.getTitle() + "방에 참여하지 못하게 되었습니다.")
                    .build();
            noticeRepository.save(memberNotice);

        }
        //방장에게 알림 (회원 나감)
        else{
            String nicknameOfParticipant = participantRepository.getNicknameOfParticipant(participantId);

            Notice masterNotice = Notice.builder()
                    .memberId(meeting.getMember().getId()) //방장에게
                    .text(meeting.getTitle() + "방에서 " +nicknameOfParticipant+"님이 나갔습니다.")
                    .link("/meetings/"+meeting.getId())
                    .build();
            noticeRepository.save(masterNotice);
        }

        //알림생성 후 참여 삭제
        participantRepository.deleteById(participantId);


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
