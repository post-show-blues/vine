package com.post_show_blues.vine.dto.follow;

import com.post_show_blues.vine.dto.member.MemberImgDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class FollowMemberResultDTO {
    private Long id;
    private String nickname;
    private String text;
    private MemberImgDTO memberImgDTO;
}
