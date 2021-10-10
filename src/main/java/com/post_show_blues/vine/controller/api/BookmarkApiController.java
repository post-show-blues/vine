package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.service.bookmark.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/meetings/{meetingId}/bookmarks")
@RequiredArgsConstructor
@RestController
public class BookmarkApiController {

    private final BookmarkService bookmarkService;

    /**
     * 북마크 등록
     */
    @PostMapping
    public ResponseEntity<?> bookmark(@PathVariable("meetingId") Long meetingId,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails){

        bookmarkService.bookmark(meetingId, principalDetails.getMember().getId());

        return new ResponseEntity<>(new CMRespDto<>(1, "북마크 등록 성공", null), HttpStatus.CREATED);
    }

    /**
     * 북마크 취소
     */
    @DeleteMapping
    public ResponseEntity<?> cancelBookmark(@PathVariable("meetingId") Long meetingId,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails){

        bookmarkService.cancelBookmark(meetingId, principalDetails.getMember().getId());

        return new ResponseEntity<>(new CMRespDto<>(1, "북마크 취소 성공", null), HttpStatus.OK);
    }


}
