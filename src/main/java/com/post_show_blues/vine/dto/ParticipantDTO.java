package com.post_show_blues.vine.dto;

import com.post_show_blues.vine.domain.memberimg.MemberImg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantDTO {

    private Long participantId;

    private Long meetingId;

    private Long memberId;

    private String nickname;

    @Builder.Default
    private String text = "";

    private Boolean req;

    private MemberImgDTO memberImgDTO;

    private LocalDateTime regDate, modDate;

}
