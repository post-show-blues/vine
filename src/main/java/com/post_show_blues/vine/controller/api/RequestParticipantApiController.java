package com.post_show_blues.vine.controller.api;


import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.requestParticipant.RequestParticipantDTO;
import com.post_show_blues.vine.service.requestParticipant.RequestParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/meetings/{meeting-id}/requests")
@Controller
public class RequestParticipantApiController {

    private final RequestParticipantService requestParticipantService;

    @GetMapping //참여요청자 목록
    public CMRespDto<?> requestParticipantList(@PathVariable("meeting-id") Long meetingId){

        List<RequestParticipantDTO> requestParticipantDTOList = requestParticipantService.getRequestParticipantList(meetingId);

        return new CMRespDto<>(1,"참여요청자 목록 불러오기 성공", requestParticipantDTOList);
    }

    @PostMapping //참여요청
    public CMRespDto<?> requestParticipant(@PathVariable("meeting-id") Long meetingId,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails){

        requestParticipantService.request(meetingId, principalDetails.getMember().getId());

        return new CMRespDto<>(1,"참여요청 성공", null);
    }

    @DeleteMapping("/{req-id}") //참여요청 거절/철회
    public CMRespDto<?> deleteRequestParticipant(@PathVariable("meeting-id") Long meetingId,
                                           @PathVariable("req-id") Long requestParticipantId){

        requestParticipantService.reject(requestParticipantId);

        return new CMRespDto<>(1, "참여요청 삭제 성공", null);
    }

}
