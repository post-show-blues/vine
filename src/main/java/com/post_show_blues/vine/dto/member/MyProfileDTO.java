package com.post_show_blues.vine.dto.member;

import com.post_show_blues.vine.dto.MemberImgDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MyProfileDTO {
    private Long id;
    private String nickname;
    private String text;
    private String instaurl;
    private String twitterurl;
    private MemberImgDTO memberImgDTO;
    //TODO : 참여활동, 팔로잉/팔로워 명수  넘겨주기 추가
    //팔로잉 / 팔로워 명수
    //참여활동 정보
}
