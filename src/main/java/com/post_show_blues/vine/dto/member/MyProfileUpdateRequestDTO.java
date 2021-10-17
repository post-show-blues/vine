package com.post_show_blues.vine.dto.member;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class MyProfileUpdateRequestDTO {
    private String nickname;
    private String email;
    private String instaurl;
    private String facebookurl;
    private String text;
    private MultipartFile file;
}
