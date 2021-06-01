package com.post_show_blues.vine.domain.meeting;


import com.post_show_blues.vine.domain.BaseEntity;
import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Meeting extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private int maxNumber;

    private int currentNumber;

    @Column(nullable = false)
    private String meetDate;

    @Column(nullable = false)
    private String reqDeadline;


    private String chatLink;
}
