package com.post_show_blues.vine.domain.comment;

import com.post_show_blues.vine.domain.BaseEntity;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","meeting","child"})
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<Comment> child  = new ArrayList<>();

    @Column(length = 100,nullable = false)
    private String content;

    //연관관계 메서드
    public void setMeeting(Meeting meeting){
        this.meeting = meeting;
        meeting.getCommentList().add(this);
    }

    public void setParent(Comment parent){
        this.parent = parent;
        parent.getChild().add(this);
    }




}
