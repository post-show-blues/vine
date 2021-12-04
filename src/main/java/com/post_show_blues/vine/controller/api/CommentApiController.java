package com.post_show_blues.vine.controller.api;

import com.post_show_blues.vine.config.auth.PrincipalDetails;
import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.dto.comment.CommentDTO;
import com.post_show_blues.vine.dto.comment.CommentReadDTO;
import com.post_show_blues.vine.handler.exception.CustomException;
import com.post_show_blues.vine.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/meetings/{meetingId}/comments")
@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    /**
     * 댓글 조회
     */
    @GetMapping
    public ResponseEntity<?> readComment(@PathVariable("meetingId") Long meetingId){

        CommentReadDTO commentReadDTO = commentService.getCommentList(meetingId);

        return new ResponseEntity<>(
                new CMRespDto<>(1, "댓글 조회 성공", commentReadDTO), HttpStatus.OK);
    }

    /**
     * 댓글 생성
     */
    @PostMapping
    public ResponseEntity<?> createComment(@PathVariable("meetingId") Long meetingId,
                                           @Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails){

        if(!commentDTO.getMemberId().equals(principalDetails.getMember().getId())){
            throw new CustomException("생성 권한이 없습니다.");
        }

        commentService.register(commentDTO, principalDetails.getMember().getId());

        return new ResponseEntity<>(
                new CMRespDto<>(1, "댓글 생성 성공", null), HttpStatus.CREATED);
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<?> modifyComment(@PathVariable("meetingId") Long meetingId,
                                           @PathVariable("commentId") Long commentId,
                                           @Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails){

        if(!commentDTO.getMemberId().equals(principalDetails.getMember().getId())){
            throw new CustomException("수정 권한이 없습니다.");
        }

        commentService.modify(commentId, commentDTO.getContent());

        return new ResponseEntity<>(
                new CMRespDto<>(1, "댓글 수정 성공", null), HttpStatus.OK);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> removeComment(@PathVariable("commentId") Long commentId,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails){

        commentService.remove(commentId);

        return new ResponseEntity<>(
                new CMRespDto<>(1, "댓글 삭제 성공", null), HttpStatus.OK);
    }

}
