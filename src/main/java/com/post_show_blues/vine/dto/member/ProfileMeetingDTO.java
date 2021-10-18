package com.post_show_blues.vine.dto.member;

import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.querydsl.core.annotations.QueryProjection;

public class ProfileMeetingDTO {
    private Long meetingId;

    private String title;

    private String text;

    private int maxNumber;

    private int currentNumber;

    private Long dDay;

    private MemberImg memberImg;
//    private List<MemberImgDTO> memberImgDTO;

    @QueryProjection
    public ProfileMeetingDTO(Long meetingId, String title, String text, int maxNumber, int currentNumber,
                             Long dDay, MemberImg memberImg) {
        this.meetingId = meetingId;
        this.title = title;
        this.text = text;
        this.maxNumber = maxNumber;
        this.currentNumber = currentNumber;
        this.dDay = dDay;
//        this.memberImgDTO = memberImgDTO;
        this.memberImg = memberImg;
    }

}
