package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.dto.member.FindMemberResultDTO;
import com.post_show_blues.vine.dto.member.MemberProfileDTO;
import com.post_show_blues.vine.dto.memberImg.MemberImgUploadDto;
import com.post_show_blues.vine.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


//회원 정보 수정
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberImgRepository memberImgRepository;

    @Value("${org.zerock.upload.path}")
    private String uploadFolder;
    private final FileStore fileStore;

    /**
     * 회원 정보 수정
     */
    @Transactional
    public Member memberUpdate(Long id, Member member) {
        // 1. 영속화
        Member memberEntity = memberRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("찾을 수 없는 id입니다")
        );

        memberEntity.setInstaurl(member.getInstaurl());
        memberEntity.setTwitterurl(member.getTwitterurl());
        memberEntity.setText(member.getText());

        return memberEntity;
    }

    @Transactional
    public void memberImgUpdate(Member member, Optional<MemberImgUploadDto> memberImgUploadDto) {
        MemberImg memberImgEntity = memberImgRepository.findByMember(member);
        MemberImg memberImg;

        //TODO : DTO가 없으면 DB 바꾸기
        //TODO : 파일시스템 내에 연결된 사진 지우기
        if (memberImgUploadDto.isEmpty()) {
            memberImg = MemberImg.builder()
                    .id(memberImgEntity.getId())
                    .member(memberImgEntity.getMember())
                    .build();
        } else {
            //회원 이미지
            UUID uuid = UUID.randomUUID(); //이미지 고유성 보장
            String imageFileName = uuid + "_" + memberImgUploadDto.get().getFile().getOriginalFilename();
            Path imageFilePath = Paths.get(uploadFolder + "/" + imageFileName);

            try {
                Files.write(imageFilePath, memberImgUploadDto.get().getFile().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }

            memberImg = MemberImg.builder()
                    .id(memberImgEntity.getId())
                    .member(memberImgEntity.getMember())
                    .fileName(imageFileName)
                    .build();
        }

        memberImgRepository.save(memberImg);

    }

    public List<FindMemberResultDTO> findMember(String keyword) {
        List<Member> members = memberRepository.findByNicknameContainingOrEmailContaining(keyword, keyword);

        if (members.isEmpty()) {
            throw new IllegalArgumentException("일치하는 회원이 없습니다");
        }
        List<FindMemberResultDTO> findMemberResultDTO = members.stream().map(m -> FindMemberResultDTO.builder()
                .id(m.getId())
                .nickname(m.getNickname())
                .build()).collect(Collectors.toList());


        return findMemberResultDTO;
    }

    public MemberProfileDTO findMemberProfile(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("일치하는 회원이 없습니다")
        );

        MemberProfileDTO memberProfileDTO = MemberProfileDTO.builder()
                .text(member.getText())
                .instaurl(member.getInstaurl())
                .nickname(member.getNickname())
                .twitterurl(member.getTwitterurl())
                .build();

        return memberProfileDTO;
    }
}
