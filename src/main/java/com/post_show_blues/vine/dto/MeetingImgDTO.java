package com.post_show_blues.vine.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingImgDTO {

    private String uuid;

    private String fileName;

    private String filePath;




}
