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

    public Comment toEntity(Long principalId){

        Comment comment;

        if(parentId == null){

            comment = Comment.builder()
                    .meeting(Meeting.builder().id(meetingId).build())
                    .member(Member.builder().id(principalId).build())
                    .content(content)
                    .build();

        }else{

            comment = Comment.builder()
                    .meeting(Meeting.builder().id(meetingId).build())
                    .member(Member.builder().id(principalId).build())
                    .parent(Comment.builder().id(parentId).build())
                    .content(content)
                    .build();
        }

        return comment;
    }



}
