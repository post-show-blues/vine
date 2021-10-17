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
    //TODO : 참여활동, 팔로잉/팔로워 명수  넘겨주기 추가

    @QueryProjection
    public MyProfileDTO(Long id, String email, String nickname, String text, String instaurl, String facebookurl, String folderPath, String storeFileName) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.text = text;
        this.instaurl = instaurl;
        this.facebookurl = facebookurl;

        if (folderPath != null && storeFileName != null) {
            this.memberImgDTO = new MemberImgDTO(folderPath, storeFileName);
        }
    }
}
