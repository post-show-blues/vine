package com.post_show_blues.vine.dto.meetingImg;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingImgDTO {

    private String storeFileName;

    private String folderPath;

    public String getImageURL() {
        try {
            return URLEncoder.encode(folderPath + "/" + storeFileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getThumbnailURL() {
        try {
            return URLEncoder.encode(folderPath + "/s_" + storeFileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }


}
