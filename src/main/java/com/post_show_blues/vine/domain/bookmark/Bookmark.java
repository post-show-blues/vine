package com.post_show_blues.vine.domain.bookmark;

import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member"})
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="bookmark_uk", //유니크 제약조건 이름
                        columnNames = {"member_id","meeting_id"}
                )
        }
)
@Entity
public class Bookmark {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    //연관관계 메서드
    public void setMeeting(Meeting meeting){
        this.meeting = meeting;
        meeting.getBookmarkList().add(this);
    }

}
