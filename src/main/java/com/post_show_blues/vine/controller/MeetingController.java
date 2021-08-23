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


    @GetMapping("/") //모임목록
    public String meetingList(@ModelAttribute PageRequestDTO requestDTO, Model model){

        PageResultDTO<MeetingDTO, Object[]> result = meetingService.getMeetingList(requestDTO);

        model.addAttribute("requestDTO", requestDTO);
        model.addAttribute("result", result);

        return "";
    }

    @GetMapping("/new") //모임등록 폼
    public String registerMeetingForm(Model model){

        List<CategoryDTO> categoryDTOList = categoryService.getCategoryList();

        model.addAttribute("categoryListDTO", categoryDTOList);

        return "";
    }

    @PostMapping("/new") //모임등록
    public String registerMeeting(@ModelAttribute MeetingDTO meetingDTO) throws IOException {

        Long meetingId = meetingService.register(meetingDTO);

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

    @PostMapping("/{meeting-id}/edit") //모임 수정
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

    @PostMapping("/{meeting-id}/delete") //모임삭제
    public String deleteMeeting(@PathVariable("meeting-id") Long meetingId){

        meetingService.remove(meetingId);

        return "redirect:/meetings";
    }



}
