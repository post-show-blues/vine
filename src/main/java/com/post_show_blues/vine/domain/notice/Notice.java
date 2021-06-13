package com.post_show_blues.vine.domain.notice;

import com.post_show_blues.vine.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Notice extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="notice_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    @Builder.Default
    private Boolean state=false;

    public void changeState(){
        this.state = true;
    }

}
