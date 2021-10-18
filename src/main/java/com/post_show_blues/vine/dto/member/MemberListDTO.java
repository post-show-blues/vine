package com.post_show_blues.vine.dto.member;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberListDTO {
    private Long id;
    private String nickname;
    private String text;
    private MemberImgDTO memberImgDTO;
    private FollowDTO followDTO;

    public MemberListDTO(Long id, String nickname, String text, Long followings, Long followers) {
        this.id = id;
        this.nickname = nickname;
        this.text = text;
        this.followDTO = new FollowDTO(followings, followers);
    }
}
