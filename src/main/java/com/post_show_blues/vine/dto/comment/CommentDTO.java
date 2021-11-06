package com.post_show_blues.vine.dto.comment;


import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Long commentId;

    @NotNull
    private Long meetingId;

    @NotNull
    private Long memberId;

    private Long parentId;

    @NotBlank
    private String content;

    @NotNull
    private Boolean open;

    public Comment toEntity(Meeting meeting, Long principalId, Comment parentComment){


        Comment comment = Comment.builder()
                .member(Member.builder().id(principalId).build())
                .content(content)
                .open(open)
                .build();

        comment.setMeeting(meeting);

        if(parentComment != null){
            comment.setParent(parentComment);

            //부모댓글이 비공개일 경우 - 자식 댓글로 비공개로 변경
            if(parentComment.getOpen() == false){
                comment.changeOpen(false);
            }
        }

        return comment;
    }



}
