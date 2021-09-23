package com.post_show_blues.vine.domain.comment;

import com.post_show_blues.vine.domain.BaseEntity;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","meeting"})
@Entity
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_comment_id", nullable = true)
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> child;

    @Column(length = 100,nullable = false)
    private String content;


}
