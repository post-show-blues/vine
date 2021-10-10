package com.post_show_blues.vine.service.comment;

import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.domain.comment.CommentRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.dto.comment.CommentDTO;
import com.post_show_blues.vine.dto.comment.CommentReadDTO;
import com.post_show_blues.vine.dto.comment.CommentResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final MeetingRepository meetingRepository;

    /**
     * 댓글쓰기
     */
    @Transactional
    @Override
    public Comment register(CommentDTO commentDTO, Long principalId) {

        Meeting meeting = meetingRepository.findById(commentDTO.getMeetingId()).orElseThrow(() ->
                new IllegalStateException("존재하지 않은 모임입니다."));


        Comment comment;

        if(commentDTO.getParentId() != null){
            //부모댓글이 있는 경우
            Comment parentComment = commentRepository.findById(commentDTO.getParentId()).orElseThrow(() ->
                    new IllegalStateException("존재하지 않은 댓글입니다."));

            comment = commentDTO.toEntity(meeting, parentComment);

        }else{ //부모댓글이 없는 경우

            comment = commentDTO.toEntity(meeting, null);
        }

        commentRepository.save(comment);

        return comment;
    }

    /**
     * 댓글수정
     */
    @Transactional
    @Override
    public void modify(Long commentId, String content) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalStateException("존재하지 않은 댓글입니다."));

        comment.changeContent(content);

    }

    /**
     * 댓글삭제
     */
    @Transactional
    @Override
    public void remove(Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalStateException("존재하지 않은 댓글입니다."));

        //existState = false
        comment.removeComment();
    }

    /**
     * 댓글 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public CommentReadDTO getCommentList(Long meetingId) {


        List<Object[]> result = commentRepository.getCommentList(meetingId);

        List<CommentResDTO> toResDTOList = result.stream().map(arr -> {
            return toResDTO((Comment) arr[0], (Member) arr[1], (MemberImg) arr[2]);
        }).collect(Collectors.toList());

        //childList 세팅
        List<CommentResDTO> commentResDTOList = new ArrayList<>();

        for(CommentResDTO commentResDTO : toResDTOList){
            if(commentResDTO.getParentId() == null){
                for(CommentResDTO childResDTO : toResDTOList){
                    if(childResDTO.getParentId() == commentResDTO.getCommentId()){
                        commentResDTO.getChildList().add(childResDTO);
                    }
                }
                commentResDTOList.add(commentResDTO);
            }
        }

        CommentReadDTO commentReadDTO =
                CommentReadDTO.builder()
                .commentCount(toResDTOList.size())
                .commentResDTOList(commentResDTOList)
                .build();

/*
        Meeting meeting = meetingRepository.findById(meetingId).get();

        List<Comment> commentList = commentRepository.getCommentBy(meeting.getId());

        List<CommentResDTO> commentResDTOList2 = new ArrayList<>();

        for (Comment comment : commentList){

            List<Comment> childList = comment.getChild();

            List<CommentResDTO> childCommentResDTOList = new ArrayList<>();

            for (Comment ChildComment : childList){

                CommentResDTO commentResDTO = CommentResDTO.builder()
                        .commentId(ChildComment.getId())
                        .parentId(ChildComment.getParent().getId())
                        .content(ChildComment.getContent())
                        .open(ChildComment.getOpen())
                        .existState(ChildComment.getExistState())
                        .memberId(ChildComment.getMember().getId())
                        .nickname(ChildComment.getMember().getNickname())
                        .regDate(ChildComment.getRegDate())
                        .modDate(ChildComment.getModDate())
                        .build();

                childCommentResDTOList.add(commentResDTO);

            }

            CommentResDTO commentResDTO = CommentResDTO.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .open(comment.getOpen())
                    .existState(comment.getExistState())
                    .memberId(comment.getMember().getId())
                    .nickname(comment.getMember().getNickname())
                    .regDate(comment.getRegDate())
                    .modDate(comment.getModDate())
                    .childList(childCommentResDTOList)
                    .build();

            commentResDTOList2.add(commentResDTO);
        }

        CommentListDTO commentListDTO = CommentListDTO.builder()
                .commentResDTOList(commentResDTOList2)
                .commentCount(5)
                .build();
*/
        return commentReadDTO;
    }


}
