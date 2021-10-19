package com.post_show_blues.vine.domain.requestParticipant;


import com.post_show_blues.vine.domain.BaseEntity;
import com.post_show_blues.vine.domain.meeting.Meeting;
import com.post_show_blues.vine.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","meeting"})
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="request_participant_uk", //유니크 제약조건 이름
                        columnNames = {"member_id","meeting_id"}
                )
        })
public class RequestParticipant extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_participant_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id",nullable = false)
    private Meeting meeting;


}
