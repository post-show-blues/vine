package com.post_show_blues.vine.dto;


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
public class MeetingDTO {

    private Long meetingId;

    private Long masterId;

    private Long categoryId;

    private String categoryName;

    private String title;

    private String text;

    private String place;

    private int maxNumber;

    private int currentNumber;

    private String meetDate;

    private String reqDeadline;

    private String chatLink;

    private LocalDateTime regDate, modDate;

    @Builder.Default
    private List<MeetingImgDTO> imgDTOList = new ArrayList<>();

}
