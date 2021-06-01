package com.post_show_blues.vine.domain.memberimg;

import com.post_show_blues.vine.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class MemberImg {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_img_id")
    private Long id;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id",nullable = false)
    private Member memberId;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

}
