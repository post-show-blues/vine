package com.post_show_blues.vine.dto.member;

import com.post_show_blues.vine.dto.MemberImgDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberListDTO {
    private Long id;
    private String nickname;
    private String text;
    private MemberImgDTO memberImgDTO;
}
