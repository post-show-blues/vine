package com.post_show_blues.vine.dto.member;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberListDTO {
    private Long id;
    private String nickname;
    private String text;
    private MemberImgDTO memberImgDTO;
}
