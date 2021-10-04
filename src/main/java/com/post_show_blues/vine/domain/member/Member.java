package com.post_show_blues.vine.domain.member;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Member implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Builder.Default
    private String text="";

    @Builder.Default
    private String instaurl="";

    @Builder.Default
    private String twitterurl="";

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Builder.Default
    private String university="";

}
