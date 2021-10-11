package com.post_show_blues.vine.controller.api;


import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.requestParticipant.RequestParticipantDTO;
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
@RequestMapping("/meetings/{meetingId}/requests")
@Controller
public class RequestParticipantApiController {

    private final RequestParticipantService requestParticipantService;

    @GetMapping //참여요청자 목록
    public ResponseEntity<?> requestParticipantList(@PathVariable("meetingId") Long meetingId){

        List<RequestParticipantDTO> requestParticipantDTOList = requestParticipantService.getRequestParticipantList(meetingId);

        return new ResponseEntity<>(new CMRespDto<>(1,"참여요청자 목록 불러오기 성공", requestParticipantDTOList), HttpStatus.OK);
    }

    @PostMapping //참여요청
    public ResponseEntity<?> requestParticipant(@PathVariable("meetingId") Long meetingId,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails){

        requestParticipantService.request(meetingId, principalDetails.getMember().getId());

        return new ResponseEntity<>(new CMRespDto<>(1,"참여요청 성공", null), HttpStatus.OK);
    }

    @DeleteMapping("/{reqId}") //참여요청 거절/철회
    public ResponseEntity<?> deleteRequestParticipant(@PathVariable("reqId") Long requestParticipantId,
                                                      @AuthenticationPrincipal PrincipalDetails principalDetails){

        requestParticipantService.reject(requestParticipantId, principalDetails.getMember().getId());

        return new ResponseEntity<>(new CMRespDto<>(1, "참여요청 삭제 성공", null), HttpStatus.OK);
    }

}
