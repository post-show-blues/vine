package com.post_show_blues.vine.dto.meeting;


import com.post_show_blues.vine.dto.member.MemberImgDTO;
import com.post_show_blues.vine.dto.meetingImg.MeetingImgDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingResDTO {

    private Long meetingId;

    private Long masterId;

    private String masterNickname;

    private String title;

    private String text;

    private String place;

    private int maxNumber;

    private int currentNumber;

    private Long dDay;

    private int commentCount;

    private Boolean bookmarkState;

    private MeetingImgDTO meetingImgDTO;

    private MemberImgDTO masterImgDTO;

}
