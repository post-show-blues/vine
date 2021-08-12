package com.post_show_blues.vine.dto.member;

import com.post_show_blues.vine.dto.MemberImgDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyProfileDTO {
    private Long id;
    private String nickname;
    private String text;
    private String instaurl;
    private String twitterurl;
    private MemberImgDTO memberImgDTO;
    //TODO : 참여활동, 팔로잉/팔로워 명수  넘겨주기 추가

    @QueryProjection
    public MyProfileDTO(Long id, String nickname, String text, String instaurl, String twitterurl,String folderPath, String storeFileName) {
        this.id = id;
        this.nickname = nickname;
        this.text = text;
        this.instaurl = instaurl;
        this.twitterurl = twitterurl;
        this.memberImgDTO = new MemberImgDTO(folderPath, storeFileName);
    }
}
