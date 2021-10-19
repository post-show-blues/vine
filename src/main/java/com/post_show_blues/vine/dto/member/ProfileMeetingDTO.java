package com.post_show_blues.vine.dto.member;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProfileMeetingDTO {
    private Long meetingId;

    private String title;

    private String text;

    private int maxNumber;

    private int currentNumber;

    private int dDay;

    private List<MemberImgDTO> memberImgDTO = new ArrayList<>();

    public ProfileMeetingDTO(Long meetingId, String title, String text, int maxNumber, int currentNumber,
                             int dDay, String folderPath1, String storeFileName1, String folderPath2, String storeFileName2) {
        this.meetingId = meetingId;
        this.title = title;
        this.text = text;
        this.maxNumber = maxNumber;
        this.currentNumber = currentNumber;
        this.dDay = dDay;
        this.memberImgDTO.add(new MemberImgDTO(folderPath1, storeFileName1));
        this.memberImgDTO.add(new MemberImgDTO(folderPath2, storeFileName2));
    }

}
