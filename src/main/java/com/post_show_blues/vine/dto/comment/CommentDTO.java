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

    public Comment toEntity(Meeting meeting, Comment parentComment){


        Comment comment = Comment.builder()
                .member(Member.builder().id(memberId).build())
                .content(content)
                .build();

        comment.setMeeting(meeting);

        if(parentComment != null){
            comment.setParent(parentComment);
        }

        return comment;
    }



}
