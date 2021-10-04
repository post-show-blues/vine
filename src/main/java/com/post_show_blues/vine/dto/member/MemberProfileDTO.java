package com.post_show_blues.vine.dto.member;

import com.post_show_blues.vine.dto.MemberImgDTO;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfileDTO {
    private String nickname;
    private String text;
    private String instaurl;
    private String facebookurl;
    private MemberImgDTO memberImgDTO;
    private Boolean isFollow;
}
