package com.post_show_blues.vine.controller;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.dto.MeetingDTO;
import com.post_show_blues.vine.dto.PageRequestDTO;
import com.post_show_blues.vine.dto.PageResultDTO;
import com.post_show_blues.vine.service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/meetings")
@Controller
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping("/register")
    public String registerMeeting(){
        return "";
    }

    @PostMapping("/register")
    public String postRegisterMeeting(MeetingDTO meetingDTO){

        meetingService.register(meetingDTO);

        return "";
    }

/*
    @GetMapping("/")
    public String listMeeting(PageRequestDTO requestDTO, Model model){


        model.addAttribute("")
    }*/

    @GetMapping("/{id}")
    public String readMeeting(@PathVariable("id") Long meetingId,
                       @AuthenticationPrincipal PrincipalDetails principalDetails,
                       Model model){

        MeetingDTO meetingDTO = meetingService.getMeeting(meetingId);

        model.addAttribute("meetingDTO", meetingDTO);

        return "";
    }

    @GetMapping("/{id}/edit")
    public String modifyMeeting(@PathVariable("id") Long meetingId, Model model){

        MeetingDTO meetingDTO = meetingService.getMeeting(meetingId);

        model.addAttribute("meetingDTO", meetingDTO);
        return "";
    }

    @PostMapping("/{id}/edit")
    public String postModifyMeeting(@PathVariable("id") Long meetingId,
                                    MeetingDTO meetingDTO){

        meetingService.modify(meetingDTO);

        return "";
    }

    @PostMapping("/{id}/delete")
    public String deleteMeeting(@PathVariable("id") Long meetingId){

        meetingService.remove(meetingId);

        return "";
    }



}
