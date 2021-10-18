package com.post_show_blues.vine.dto.auth;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.dto.member.MemberImgDTO;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponse {
    private Long id;
    private String nickname;
    private String email;
    private MemberImgDTO file;

    public SignupResponse(Member member, MemberImg memberImg) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        if (memberImg != null) {
            this.file = MemberImgDTO.builder()
                    .folderPath(memberImg.getFolderPath())
                    .storeFileName(memberImg.getStoreFileName())
                    .build();
        }
    }
}