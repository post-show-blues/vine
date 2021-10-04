package com.post_show_blues.vine.dto.member;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@ToString
public class MemberUpdateDto {
    private String instaurl;
    private String facebookurl;
    private String text;
    private MultipartFile file;


    public Member toEntity() {
        return Member.builder()
                .text(text)
                .instaurl(instaurl)
                .facebookurl(facebookurl)
                .build();
    }

    public MemberImg toMemberImgEntity(String folderPath, String storeFileName, Member member){
        return MemberImg.builder()
                .member(member)
                .folderPath(folderPath)
                .storeFileName(storeFileName)
                .build();
    }
}
