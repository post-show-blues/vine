package com.post_show_blues.vine.dto;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeResultDTO {

    private Long memberId;

    private String text;

    private String link;

    public Notice toEntity() {
        return Notice.builder()
                .memberId(memberId)
                .text(text)
                .link(link)
                .build();
    }

}

