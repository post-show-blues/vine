package com.post_show_blues.vine.dto.memberImg;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberImgUploadDto {
    private MultipartFile file;

    public MemberImg toEntity(String memberImgUrl, Member member){
        return MemberImg.builder()
                .member(member)
                .fileName(memberImgUrl)
                .build();
    }
}
