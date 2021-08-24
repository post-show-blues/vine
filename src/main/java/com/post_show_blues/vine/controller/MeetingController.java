package com.post_show_blues.vine.controller;

import com.post_show_blues.vine.dto.CategoryDTO;
import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.service.category.CategoryService;
import com.post_show_blues.vine.service.meeting.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/meetings")
@Controller
public class MeetingController {

    private final MeetingService meetingService;
    private final CategoryService categoryService;


    @GetMapping("/new") //모임등록 폼
    public String registerMeetingForm(Model model){

        List<CategoryDTO> categoryDTOList = categoryService.getCategoryList();

        model.addAttribute("categoryListDTO", categoryDTOList);

        return "";
    }



    @GetMapping("/{meeting-id}/edit") //모임수정 폼
    public String modifyMeetingForm(@PathVariable("meeting-id") Long meetingId,
                                    @ModelAttribute PageRequestDTO requestDTO,
                                    Model model){

        MeetingDTO meetingDTO = meetingService.getMeeting(meetingId);
        List<CategoryDTO> categoryDTOList = categoryService.getCategoryList();

        model.addAttribute("requestDTO", requestDTO);
        model.addAttribute("meetingDTO", meetingDTO);
        model.addAttribute("categoryListDTO", categoryDTOList);

        return "";
    }



}
