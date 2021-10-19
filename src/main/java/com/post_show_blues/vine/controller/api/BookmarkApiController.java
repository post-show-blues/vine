package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.meeting.MeetingResDTO;
import com.post_show_blues.vine.dto.page.PageRequestDTO;
import com.post_show_blues.vine.dto.page.PageResultDTO;
import com.post_show_blues.vine.handler.exception.CustomException;
import com.post_show_blues.vine.service.bookmark.BookmarkService;
import com.post_show_blues.vine.service.meeting.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BookmarkApiController {

    private final BookmarkService bookmarkService;
    private final MeetingService meetingService;

    /**
     * 북마크 조회 (회원별)
     */
    @GetMapping("/bookmarks")
    public ResponseEntity<?> bookmarkList(PageRequestDTO requestDTO,
                                          @AuthenticationPrincipal PrincipalDetails principalDetails){

        if(!requestDTO.getMemberId().equals(principalDetails.getMember().getId())){
            throw new CustomException("조회 권한이 없습니다.");
        }

        PageResultDTO<MeetingResDTO, Object[]> result = meetingService.getBookmarkMeetingList(
                requestDTO, principalDetails.getMember().getId());

        return new ResponseEntity<>(new CMRespDto<>(1,"북마크 조회 성공", result), HttpStatus.OK);
    }

    /**
     * 북마크 등록
     */
    @PostMapping("/meetings/{meetingId}/bookmarks")
    public ResponseEntity<?> bookmark(@PathVariable("meetingId") Long meetingId,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails){

        bookmarkService.bookmark(meetingId, principalDetails.getMember().getId());

        return new ResponseEntity<>(new CMRespDto<>(1, "북마크 등록 성공", null), HttpStatus.CREATED);
    }

    /**
     * 북마크 취소
     */
    @DeleteMapping("/meetings/{meetingId}/bookmarks")
    public ResponseEntity<?> cancelBookmark(@PathVariable("meetingId") Long meetingId,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails){

        bookmarkService.cancelBookmark(meetingId, principalDetails.getMember().getId());

        return new ResponseEntity<>(new CMRespDto<>(1, "북마크 취소 성공", null), HttpStatus.OK);
    }


}
