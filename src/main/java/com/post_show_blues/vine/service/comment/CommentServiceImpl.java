package com.post_show_blues.vine.service.comment;

import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.domain.comment.CommentRepository;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.meeting.MeetingRepository;
import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.dto.comment.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
