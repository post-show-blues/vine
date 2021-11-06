package com.post_show_blues.vine.dto.meetingImg;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingImgUploadDTO {

    private MultipartFile[] uploadFiles;


}
