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
public class follow {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="follow_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="from_user_id",nullable = false)
    private Member fromUserId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="to_user_id",nullable = false)
    private Member toUserId;
}
