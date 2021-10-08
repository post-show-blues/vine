package com.post_show_blues.vine.service.comment;

import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.dto.comment.CommentDTO;
import com.post_show_blues.vine.dto.comment.CommentListDTO;
import com.post_show_blues.vine.dto.comment.CommentResDTO;
import com.post_show_blues.vine.dto.member.MemberImgDTO;

public interface CommentService {

    Comment register(CommentDTO commentDTO, Long principalId);

    void modify(Long commentId, String content);

    void remove(Long commentId);

    CommentListDTO getCommentList(Long meetingId);

    default CommentResDTO toResDTO(Comment comment, Member writer, MemberImg writerImg){

        //childList 처리 x
        CommentResDTO commentResDTO = CommentResDTO.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .open(comment.getOpen())
                .existState(comment.getExistState())
                .memberId(writer.getId())
                .nickname(writer.getNickname())
                .regDate(comment.getRegDate())
                .modDate(comment.getModDate())
                .build();

        if(comment.getParent() != null){
            commentResDTO.setParentId(comment.getParent().getId());
        }

        if(writerImg != null){
            MemberImgDTO writerImgDTO = MemberImgDTO.builder()
                    .folderPath(writerImg.getFolderPath())
                    .storeFileName(writerImg.getStoreFileName())
                    .build();

            commentResDTO.setMemberImgDTO(writerImgDTO);
        }

        return commentResDTO;
    }
}
