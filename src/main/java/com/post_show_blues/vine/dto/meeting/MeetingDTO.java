package com.post_show_blues.vine.dto.meeting;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.post_show_blues.vine.dto.meetingImg.MeetingImgDTO;
import com.post_show_blues.vine.dto.MemberImgDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

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

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime meetDate;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime reqDeadline;

    private int dDay;

    private String chatLink;

    private LocalDateTime regDate, modDate;

    private List<MultipartFile> imageFiles;

    @Builder.Default
    private List<MeetingImgDTO> imgDTOList = new ArrayList<>();

    private MemberImgDTO masterImg;

    private MemberImgDTO participantImg;

}
