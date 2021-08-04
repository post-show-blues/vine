package com.post_show_blues.vine.dto.requestParticipant;

import com.post_show_blues.vine.dto.MemberImgDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestParticipantDTO {

    private Long requestParticipantId;

    private Long meetingId;

    private Long memberId;

    private String nickname;

    @Builder.Default
    private String text = "";

    private MemberImgDTO memberImgDTO;

    private LocalDateTime regDate;
}
