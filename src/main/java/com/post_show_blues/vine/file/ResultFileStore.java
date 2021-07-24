package com.post_show_blues.vine.file;

import com.post_show_blues.vine.domain.meetingimg.MeetingImg;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultFileStore {

    private String folderPath;

    private String storeFileName;

}
