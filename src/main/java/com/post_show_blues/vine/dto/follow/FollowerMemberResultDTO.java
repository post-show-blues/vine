package com.post_show_blues.vine.dto.follow;

import com.post_show_blues.vine.dto.MemberImgDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class FollowerMemberResultDTO {
    private Long id;
    private String nickname;
    private String text;
    private Boolean isFollow;
    private MemberImgDTO memberImgDTO;
}