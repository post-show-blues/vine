package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.requestParticipant.RequestParticipant;
import com.post_show_blues.vine.dto.MemberImgDTO;
import com.post_show_blues.vine.dto.requestParticipant.RequestParticipantDTO;

import java.util.List;

public interface RequestParticipantService {

    Long request(Long meetingId, Long memberId);

    void accept(Long requestParticipantId);

    void reject(Long requestParticipantId);

    List<RequestParticipantDTO> getRequestParticipantList(Long meetingId);

    RequestParticipant findOne(Long requestParticipantId);


    default RequestParticipantDTO entityToDTO(MemberImg memberImg,
                                       Member member,
                                       RequestParticipant requestParticipant){

        RequestParticipantDTO requestParticipantDTO = RequestParticipantDTO.builder()
                .requestParticipantId(requestParticipant.getId())
                .meetingId(requestParticipant.getMeeting().getId())
                .memberId(member.getId())
                .nickname(member.getNickname())
                .text(member.getText())
                .regDate(requestParticipant.getRegDate())
                .build();

        if(memberImg != null){
            MemberImgDTO memberImgDTO = MemberImgDTO.builder()
                    .filePath(memberImg.getFilePath())
                    .fileName(memberImg.getFileName())
                    .uuid(memberImg.getUuid())
                    .uuid(memberImg.getUuid())
                    .build();

            requestParticipantDTO.setMemberImgDTO(memberImgDTO);
        }

        return requestParticipantDTO;
    }


}
