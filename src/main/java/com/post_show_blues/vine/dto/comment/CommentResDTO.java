package com.post_show_blues.vine.dto.comment;

import com.post_show_blues.vine.dto.member.MemberImgDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResDTO {

    private Long commentId;

    private String content;

    private Boolean open;

    private Boolean existState;

    private Long parentId;

    @Builder.Default
    private List<CommentResDTO> childList = new ArrayList<>();

    private Long memberId;

    private String nickname;

    private MemberImgDTO memberImgDTO;

    private LocalDateTime regDate, modDate;

}
