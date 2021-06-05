package com.post_show_blues.vine.dto.member;

import com.post_show_blues.vine.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MemberUpdateDto {
    private String text;
    private String instaurl;
    private String twitterurl;

    public Member toEntity() {
        return Member.builder()
                .text(text)
                .instaurl(instaurl)
                .twitterurl(twitterurl)
                .build();
    }
}
