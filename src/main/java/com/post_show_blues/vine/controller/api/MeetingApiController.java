package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.meeting.DetailMeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingDTO;
import com.post_show_blues.vine.dto.meeting.MeetingResDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.handler.exception.CustomException;
import com.post_show_blues.vine.service.meeting.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Log4j2
@RequestMapping("/meetings")
@RequiredArgsConstructor
@RestController
public class MeetingApiController {

    private final MeetingService meetingService;

    /**
     *모임 리스트 조회
     *
     * 경우의 수
     * 1. 미로그인 상태
     * 2. 로그인 상태
     * 3. 팔로우 모임 리스트
     */

    @GetMapping//모임목록
    public ResponseEntity<?> meetingList(PageRequestDTO requestDTO,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails){

        PageResultDTO<MeetingResDTO, Object[]> result;

        //전체 모임리스트 조회
        if(requestDTO.getUserId() == null){

            //로그인하지 않은 상태
            if(principalDetails == null){
                result = meetingService.getAllMeetingList(requestDTO, null);
            }
            //로그인한 상태
            else{
                result = meetingService.getAllMeetingList(requestDTO, principalDetails.getMember().getId());
            }

            return new ResponseEntity<>(new CMRespDto<>(1, "모임 목록 불러오기 성공", result), HttpStatus.OK);
        }

       //팔로우가 방장인 모임리스트 조회
        else {

            if(!requestDTO.getUserId().equals(principalDetails.getMember().getId())){
                throw new CustomException("조회 권한이 없습니다.");
            }
            result = meetingService.getFollowMeetingList(requestDTO, principalDetails.getMember().getId());

            return new ResponseEntity<>(new CMRespDto<>(1, "팔로우 모임 목록 불러오기 성공", result), HttpStatus.OK);
        }
    }

    @PostMapping //모임등록
    public ResponseEntity<?> registerMeeting(@Valid MeetingDTO meetingDTO, BindingResult bindingResult) throws IOException {

        meetingService.register(meetingDTO);

        return new ResponseEntity<>(new CMRespDto<>(1, "모임등록 성공", null), HttpStatus.CREATED);
    }

    @GetMapping("/{meetingId}") //모임조회
    public ResponseEntity<?> readMeeting(@PathVariable("meetingId") Long meetingId,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails){

        DetailMeetingDTO detailMeetingDTO =
                meetingService.getMeeting(meetingId, principalDetails.getMember().getId());

        return new ResponseEntity<>(new CMRespDto<>(1, "모임 상세보기 성공", detailMeetingDTO), HttpStatus.OK);
    }

    @PutMapping("/{meetingId}") //모임 수정
    public ResponseEntity<?> ModifyMeeting(@PathVariable("meetingId") Long meetingId,
                                           @Valid MeetingDTO meetingDTO, BindingResult bindingResult) throws IOException {

        meetingService.modify(meetingDTO);

        return new ResponseEntity<>(new CMRespDto<>(1, "모임수정 성공", null), HttpStatus.OK);
    }

    @DeleteMapping("/{meetingId}") //모임삭제
    public ResponseEntity<?> deleteMeeting(@PathVariable("meetingId") Long meetingId){

        meetingService.remove(meetingId);

        return new ResponseEntity<>(new CMRespDto<>(1, "모임삭제 성공", null), HttpStatus.OK);
    }

}
