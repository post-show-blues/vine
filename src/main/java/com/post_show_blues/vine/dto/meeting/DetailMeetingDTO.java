package com.post_show_blues.vine.dto.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.post_show_blues.vine.domain.category.Category;
import com.post_show_blues.vine.dto.meetingImg.MeetingImgDTO;
import com.post_show_blues.vine.dto.member.MemberListDTO;
import com.post_show_blues.vine.dto.participant.ParticipantDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailMeetingDTO {

    private Long meetingId;

    @Enumerated(EnumType.STRING)
    private Category category;

    private int commentCount;

    private Boolean bookmarkState;

    private Boolean heartState;

    private int heartCount;

    private String title;

    private String text;

    private String place;

    private int maxNumber;

    private int currentNumber;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime meetDate;

    private Long dDay;

    private String chatLink;

    private MemberListDTO masterDTO;

    @Builder.Default
    private List<ParticipantDTO> participantDTOList = new ArrayList<>();

    @Builder.Default
    private List<MeetingImgDTO> imgDTOList = new ArrayList<>();

}
