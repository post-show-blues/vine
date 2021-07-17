package com.post_show_blues.vine.dto.follow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class FollowerMemberResultDTO {
    private Long id;
    private String nickname;

    @Builder.Default
    private String text = "";

    @Builder.Default
    private String imgFileName = "";

    private Boolean isFollow;
}
