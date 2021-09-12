package com.post_show_blues.vine.controller.api;


import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.participant.ParticipantDTO;
import com.post_show_blues.vine.service.participant.ParticipantService;
import com.post_show_blues.vine.service.requestParticipant.RequestParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/meetings/{meeting-id}/participant")
@Controller
public class ParticipantApiController {

    private final ParticipantService participantService;
    private final RequestParticipantService requestParticipantService;


    @GetMapping //참여인원 목록
    public CMRespDto<?> requestParticipantList(
                                                @PathVariable("meeting-id") Long meetingId){

        
        List<ParticipantDTO> participantDTOList = participantService.getParticipantList(meetingId);

        return new CMRespDto<>(1, "참여인원 목록 불러오기 성공", participantDTOList);
    }

    @PostMapping //참여수락
    public CMRespDto<?> requestParticipant(@PathVariable("meeting-id") Long meetingId,
                                           @RequestParam("req-id") Long requestParticipantId){

        requestParticipantService.accept(requestParticipantId);

        return new CMRespDto<>(1, "참여수락 성공", null);
    }

    @DeleteMapping("/{par-id}") //참여인원 추방/나가기
    public CMRespDto<?> deleteRequestParticipant(@PathVariable("meeting-id") Long meetingId,
                                           @PathVariable("par-id") Long participantId,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails){

        participantService.remove(participantId, principalDetails.getMember().getId());

        return new CMRespDto<>(1, "참여인원삭제 성공", null);
    }
}
