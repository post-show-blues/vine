package com.post_show_blues.vine.dto.member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowDTO {
    private Long followings;
    private Long followers;

    public FollowDTO(Long followings, Long followers) {
        if (followings == null) this.followings = 0L;
        else this.followings = followings;

        if (followers == null) this.followers = 0L;
        else this.followers = followers;
    }
}
