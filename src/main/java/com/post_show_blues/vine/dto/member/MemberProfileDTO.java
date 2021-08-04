package com.post_show_blues.vine.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@Builder
public class MemberProfileDTO {
    private String nickname;
    private String text;
    private String instaurl;
    private String twitterurl;
    //TODO : 사진, 참여활동, 팔로잉/팔로워 명수  넘겨주기 추가
    //팔로잉 / 팔로워 명수
    //참여활동 정보
//    private MultipartFile file;
}
