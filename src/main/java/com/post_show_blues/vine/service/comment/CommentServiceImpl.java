package com.post_show_blues.vine.service.comment;

import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.domain.comment.CommentRepository;
import com.post_show_blues.vine.dto.comment.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;


    /**
     * 댓글쓰기
     */
    @Transactional
    @Override
    public Comment register(CommentDTO commentDTO, Long principalId) {

        Comment comment = commentDTO.toEntity(principalId);

        commentRepository.save(comment);

        return comment;
    }




}
