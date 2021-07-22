package com.post_show_blues.vine.controller.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/meetings/{meeting-id}/requests")
@Controller
public class RequestParticipantApiController {

    @GetMapping //참여요청자 목록
    public String requestParticipantList(@PathVariable("meeting-id") Long memberId){

        return "";
    }

    @PostMapping //참여요청
    public String requestParticipant(@PathVariable("meeting-id") Long memberId){

        return "";
    }

    @DeleteMapping("/{req-id}") //참여요청 거절/철회
    public String deleteRequestParticipant(@PathVariable("meeting-id") Long memberId,
                                           @PathVariable("req-id") Long requestParticipantId){

        return "";
    }

}
