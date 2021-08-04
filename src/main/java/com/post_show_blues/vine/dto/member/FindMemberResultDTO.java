package com.post_show_blues.vine.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FindMemberResultDTO {
    private long id;
    private String nickname;
    //TODO : 사진 화면으로 전송
    //private MultipartFile file;
}
