package com.post_show_blues.vine.controller.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/meetings/{meeting-id}/participant")
@Controller
public class ParticipantApiController {


    @GetMapping //참여인원 목록
    public String requestParticipantList(@PathVariable("meeting-id") Long memberId){

        return "";
    }

    @PostMapping //참여수락
    public String requestParticipant(@PathVariable("meeting-id") Long memberId){

        return "";
    }

    @DeleteMapping("/{req-id}") //참여인원 추방/나가기
    public String deleteRequestParticipant(@PathVariable("meetig-id") Long memberId,
                                           @PathVariable("req-id") Long requestParticipantId){

        return "";
    }
}
