package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.meeting.DetailMeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingResDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.service.meeting.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@Log4j2
@RequestMapping("/meetings")
@RequiredArgsConstructor
@RestController
public class MeetingApiController {

    private final MeetingService meetingService;

    @GetMapping//모임목록
    public ResponseEntity<?> meetingList(PageRequestDTO requestDTO,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails){

        PageResultDTO<MeetingResDTO, Object[]> result;

        //전체 모임리스트 조회
        if(requestDTO.getUserId() == null){

            result = meetingService.getAllMeetingList(requestDTO, principalDetails.getMember().getId());

            return new ResponseEntity<>(new CMRespDto<>(1, "모임 목록 불러오기 성공", result), HttpStatus.OK);
        }

       //팔로우가 방장인 모임리스트 조회
        else {

            if(!requestDTO.getUserId().equals(principalDetails.getMember().getId())){
                throw new IllegalStateException("조회 권한이 없습니다.");
            }
            result = meetingService.getFollowMeetingList(requestDTO, principalDetails.getMember().getId());

            return new ResponseEntity<>(new CMRespDto<>(1, "팔로우 모임 목록 불러오기 성공", result), HttpStatus.OK);
        }
    }

    @PostMapping //모임등록
    public ResponseEntity<?> registerMeeting(MeetingDTO meetingDTO) throws IOException {

        meetingService.register(meetingDTO);

        return new ResponseEntity<>(new CMRespDto<>(1, "모임등록 성공", null), HttpStatus.CREATED);
    }

    @GetMapping("/{meeting-id}") //모임조회
    public ResponseEntity<?> readMeeting(@PathVariable("meeting-id") Long meetingId,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails){

        DetailMeetingDTO detailMeetingDTO =
                meetingService.getMeeting(meetingId, principalDetails.getMember().getId());

        return new ResponseEntity<>(new CMRespDto<>(1, "모임 상세보기 성공", detailMeetingDTO), HttpStatus.OK);
    }

    @PutMapping("/{meeting-id}") //모임 수정
    public ResponseEntity<?> ModifyMeeting(@PathVariable("meeting-id") Long meetingId,
                                           @RequestBody MeetingDTO meetingDTO) throws IOException {

        meetingService.modify(meetingDTO);

        return new ResponseEntity<>(new CMRespDto<>(1, "모임수정 성공", null), HttpStatus.OK);
    }

    @DeleteMapping("/{meeting-id}") //모임삭제
    public ResponseEntity<?> deleteMeeting(@PathVariable("meeting-id") Long meetingId){

        meetingService.remove(meetingId);

        return new ResponseEntity<>(new CMRespDto<>(1, "모임삭제 성공", null), HttpStatus.OK);
    }

}
