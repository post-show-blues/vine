package com.post_show_blues.vine.dto.member;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateResultDTO {
    private Long id;
    private String nickname;
    private String email;
    private MemberImgDTO memberImgDTO;

    public MemberUpdateResultDTO(Member member, MemberImg memberImg) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        if (memberImg != null) {
            this.memberImgDTO = MemberImgDTO.builder()
                    .folderPath(memberImg.getFolderPath())
                    .storeFileName(memberImg.getStoreFileName())
                    .build();
        }
    }
}