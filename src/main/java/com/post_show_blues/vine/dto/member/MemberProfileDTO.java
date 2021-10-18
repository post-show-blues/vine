package com.post_show_blues.vine.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfileDTO {
    private Long id;
    private String email;
    private String nickname;
    private String text;
    private String instaurl;
    private String facebookurl;
    private MemberImgDTO memberImgDTO;
    private FollowDTO followDTO;
    private Boolean isFollow;

    @QueryProjection
    public MemberProfileDTO(Long id, String email, String nickname, String text, String instaurl, String facebookurl, String folderPath, String storeFileName, Long following, Long follower, Boolean isFollow) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.text = text;
        this.instaurl = instaurl;
        this.facebookurl = facebookurl;
        this.followDTO = new FollowDTO(following, follower);
        this.memberImgDTO = new MemberImgDTO(folderPath, storeFileName);
        this.isFollow = isFollow;

    }
}
