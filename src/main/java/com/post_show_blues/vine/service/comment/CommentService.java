package com.post_show_blues.vine.service.comment;

import com.post_show_blues.vine.domain.comment.Comment;
import com.post_show_blues.vine.dto.comment.CommentDTO;

public interface CommentService {

    Comment register(CommentDTO commentDTO, Long principalId);
}
