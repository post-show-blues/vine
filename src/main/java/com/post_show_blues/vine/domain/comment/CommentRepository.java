package com.post_show_blues.vine.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.id = :commentId")
    Comment findByCommentId(Long commentId);

    @Query("select c, m, mi from Comment c " +
            "left join Member m on m.id = c.member.id " +
            "left join MemberImg mi on mi.member.id = m.id " +
            "where c.meeting.id = :meetingId")
    List<Object[]> getCommentList(Long meetingId);

    @Query("select c from Comment c where c.meeting.id = :meetingId and c.parent IS Null")
    List<Comment> getCommentBy(Long meetingId);
}
