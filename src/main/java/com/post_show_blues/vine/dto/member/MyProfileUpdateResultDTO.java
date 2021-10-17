package com.post_show_blues.vine.dto.member;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MyProfileUpdateResultDTO {
    private String nickname;
    private String email;
    private String instaurl;
    private String facebookurl;
    private String text;
    private MemberImgDTO file;
}

