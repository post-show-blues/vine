package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.service.meeting.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

@Log4j2
@RequestMapping("/meetings")
@RequiredArgsConstructor
@RestController
public class MeetingApiController {

    private final MeetingService meetingService;

    //TODO - api 통신 방식 상의 필요

    @GetMapping//모임목록
    public String meetingList(@RequestBody PageRequestDTO requestDTO, Model model){

        PageResultDTO<MeetingDTO, Object[]> result = meetingService.getMeetingList(requestDTO);

        model.addAttribute("requestDTO", requestDTO);
        model.addAttribute("result", result);

        return "";
    }

    @PostMapping //모임등록
    public String registerMeeting(@RequestBody MeetingDTO meetingDTO) throws IOException {

        meetingService.register(meetingDTO);

        return "redirect:/meetings";
    }

    @GetMapping("/{meeting-id}") //모임조회
    public String readMeeting(@PathVariable("meeting-id") Long meetingId,
                              @ModelAttribute PageRequestDTO requestDTO,
                              Model model){

        MeetingDTO meetingDTO = meetingService.getMeeting(meetingId);

        model.addAttribute("requestDTO", requestDTO);
        model.addAttribute("meetingDTO", meetingDTO);

        return "";
    }

    @PutMapping("/{meeting-id}") //모임 수정
    public String ModifyMeeting(@PathVariable("meeting-id") Long meetingId,
                                @ModelAttribute PageRequestDTO requestDTO,
                                MeetingDTO meetingDTO, Model model,
                                RedirectAttributes redirectAttributes) throws IOException {

        meetingService.modify(meetingDTO);

        model.addAttribute("requestDTO", requestDTO);

        redirectAttributes.addAttribute("meetingId",meetingId);
        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("category",requestDTO.getCategory());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());

        return "redirect:/read/{meetingId}";
    }

    @DeleteMapping("/{meeting-id}") //모임삭제
    public String deleteMeeting(@PathVariable("meeting-id") Long meetingId){

        meetingService.remove(meetingId);

        return "redirect:/meetings";
    }

}