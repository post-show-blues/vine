package com.post_show_blues.vine.domain.follow;
import com.post_show_blues.vine.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="follow_uk",
                        columnNames={"from_member_id", "to_member_id"}
                )
        }
)
public class Follow {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="follow_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="from_member_id",nullable = false)
    private Member fromMemberId; //팔로우 하는 멤버

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="to_member_id",nullable = false)
    private Member toMemberId; //팔로우 받는 멤버
}
