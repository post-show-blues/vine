package com.post_show_blues.vine.dto.member;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class MemberImgDTO {
    private String folderPath;
    private String storeFileName;

    public MemberImgDTO(String folderPath, String storeFileName) {
        if (folderPath != null && storeFileName != null) {
            this.folderPath = folderPath;
            this.storeFileName = storeFileName;
        }
    }
}
