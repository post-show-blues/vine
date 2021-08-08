package com.post_show_blues.vine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberImgDTO {
    private String folderPath;
    private String storeFileName;

}
