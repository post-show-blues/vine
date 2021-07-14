package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.participant.Participant;
import com.post_show_blues.vine.dto.MemberImgDTO;
import com.post_show_blues.vine.dto.participant.ParticipantDTO;

import java.util.List;

public interface ParticipantService {

    void remove(Long participantId, Long memberId); //2번째 파라미터 = 현재 로그인 id (알림구분 용도)

    List<ParticipantDTO> getParticipantList(Long meetingId);

    Participant findOne(Long id);

    default ParticipantDTO entityToDTO(MemberImg memberImg, Member member, Participant participant){

        ParticipantDTO participantDTO = ParticipantDTO.builder()
                .participantId(participant.getId())
                .meetingId(participant.getMeeting().getId())
                .memberId(member.getId())
                .nickname(member.getNickname())
                .text(member.getText())
                .regDate(participant.getRegDate())
                .modDate(participant.getModDate())
                .build();

        if(memberImg != null){
            MemberImgDTO memberImgDTO = MemberImgDTO.builder()
                    .filePath(memberImg.getFilePath())
                    .fileName(memberImg.getFileName())
                    .uuid(memberImg.getUuid())
                    .uuid(memberImg.getUuid())
                    .build();

            participantDTO.setMemberImgDTO(memberImgDTO);
        }

        return participantDTO;
    }
}
