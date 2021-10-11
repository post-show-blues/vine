package com.post_show_blues.vine.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentReadDTO {

    private int commentCount;

    @Builder.Default
    private List<CommentResDTO> commentResDTOList = new ArrayList<>();
}
