package com.post_show_blues.vine.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyProfileDTO {
    private Long id;
    private String email;
    private String nickname;
    private String text;
    private String instaurl;
    private String facebookurl;
    private MemberImgDTO memberImgDTO;
    private FollowDTO followDTO;

    @QueryProjection
    public MyProfileDTO(Long id, String email, String nickname, String text, String instaurl, String facebookurl, String folderPath, String storeFileName, Long following, Long follower) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.text = text;
        this.instaurl = instaurl;
        this.facebookurl = facebookurl;
        this.followDTO = new FollowDTO(following, follower);
        this.memberImgDTO = new MemberImgDTO(folderPath, storeFileName);
    }
}
