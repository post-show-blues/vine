package com.post_show_blues.vine.controller;

import com.post_show_blues.vine.dto.CategoryDTO;
import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.service.CategoryService;
import com.post_show_blues.vine.service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/meetings")
@Controller
public class MeetingController {

    private final MeetingService meetingService;
    private final CategoryService categoryService;


    @GetMapping("/") //모임목록
    public String meetingList(PageRequestDTO requestDTO, Model model){


        model.addAttribute("");
        return "";
    }

    @GetMapping("/new") //모임등록 폼
    public String registerMeetingForm(Model model){

        List<CategoryDTO> categoryDTOList = categoryService.getCategoryList();

        model.addAttribute("categoryListDTO", categoryDTOList);

        return "";
    }

    @PostMapping("/new") //모임등록
    public String registerMeeting(MeetingDTO meetingDTO){

        meetingService.register(meetingDTO);

        return "";
    }

    @GetMapping("/{meeting-id}") //모임조회
    public String readMeeting(@PathVariable("meeting-id") Long meetingId, Model model){

        MeetingDTO meetingDTO = meetingService.getMeeting(meetingId);

        model.addAttribute("meetingDTO", meetingDTO);

        return "";
    }

    @GetMapping("/{meeting-id}/edit") //모임수정 폼
    public String modifyMeetingForm(@PathVariable("meeting-id") Long meetingId, Model model){

        MeetingDTO meetingDTO = meetingService.getMeeting(meetingId);
        List<CategoryDTO> categoryDTOList = categoryService.getCategoryList();

        model.addAttribute("meetingDTO", meetingDTO);
        model.addAttribute("categoryListDTO", categoryDTOList);

        return "";
    }

    @PostMapping("/{meeting-id}/edit") //모임 수정
    public String ModifyMeeting(@PathVariable("meeting-id") Long meetingId,
                                    MeetingDTO meetingDTO){

        meetingService.modify(meetingDTO);

        return "";
    }

    @PostMapping("/{meeting-id}/delete") //모임삭제
    public String deleteMeeting(@PathVariable("meeting-id") Long meetingId){

        meetingService.remove(meetingId);

        return "";
    }



}
