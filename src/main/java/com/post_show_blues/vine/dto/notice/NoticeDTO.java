package com.post_show_blues.vine.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {

    private Long noticeId;

    private Long memberId;

    private String text;

    private String link;

    private Boolean readState;

}
