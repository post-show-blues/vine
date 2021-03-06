package com.post_show_blues.vine.controller.api;


import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.participant.ParticipantDTO;
import com.post_show_blues.vine.dto.requestParticipant.RequestParticipantDTO;
import com.post_show_blues.vine.service.participant.ParticipantService;
import com.post_show_blues.vine.service.requestParticipant.RequestParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/meetings/{meetingId}/participants")
@Controller
public class ParticipantApiController {

    private final ParticipantService participantService;
    private final RequestParticipantService requestParticipantService;


    @GetMapping //참여인원 목록
    public ResponseEntity<?> requestParticipantList(
                                                @PathVariable("meetingId") Long meetingId){

        
        List<ParticipantDTO> participantDTOList = participantService.getParticipantList(meetingId);

        return new ResponseEntity<>(new CMRespDto<>(1, "참여인원 목록 불러오기 성공", participantDTOList), HttpStatus.OK);
    }

    @PostMapping //참여수락
    public ResponseEntity<?> requestParticipant(@PathVariable("meetingId") Long meetingId,
                                                @RequestBody RequestParticipantDTO requestParticipantDTO,
                                                @AuthenticationPrincipal PrincipalDetails principalDetails){

        requestParticipantService.accept(requestParticipantDTO.getRequestParticipantId(), principalDetails.getMember().getId());

        return new ResponseEntity<>(new CMRespDto<>(1, "참여수락 성공", null), HttpStatus.OK);
    }

    @DeleteMapping("/{parId}") //참여인원 추방/나가기
    public ResponseEntity<?> deleteRequestParticipant(@PathVariable("parId") Long participantId,
                                                      @AuthenticationPrincipal PrincipalDetails principalDetails){

        participantService.remove(participantId, principalDetails.getMember().getId());

        return new ResponseEntity<>(new CMRespDto<>(1, "참여인원삭제 성공", null), HttpStatus.OK);
    }
}
