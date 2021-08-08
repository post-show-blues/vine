package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.member.SearchMemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.dto.MemberImgDTO;
import com.post_show_blues.vine.dto.member.MemberListDTO;
import com.post_show_blues.vine.dto.member.MemberProfileDTO;
import com.post_show_blues.vine.dto.member.MyProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


//회원 정보 수정
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final SearchMemberRepository searchMemberRepository;


    @Transactional(readOnly = true)
    public List<MemberListDTO> memberList(String keyword) {
        List<Object[]> members = memberRepository.findMemberByNicknameOrEmail(keyword);


        if (members.isEmpty()) {
            throw new IllegalArgumentException("일치하는 회원이 없습니다");
        }
        List<MemberListDTO> memberListDTO = members.stream().map(m -> {
            Member member = (Member) m[0];
            MemberImg memberImg = (MemberImg) m[1];

            if (memberImg != null) {
                MemberImgDTO memberImgDTO = MemberImgDTO.builder()
                        .folderPath(memberImg.getFolderPath())
                        .storeFileName(memberImg.getStoreFileName())
                        .build();

                return MemberListDTO.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .text(member.getText())
                        .memberImgDTO(memberImgDTO)
                        .build();
            } else {
                return MemberListDTO.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .text(member.getText())
                        .build();
            }
        }).collect(Collectors.toList());

        return memberListDTO;
    }

    @Transactional(readOnly = true)
    public MemberProfileDTO memberProfile(Long myId, Long findId) {

        //TODO : 없는 회원 찾을 때(ID 미스)
        //TODO : 사진이 없는 회원 찾을 때
        //TODO : 팔로우하지 않은 회원 찾을 때 테스트

        MemberProfileDTO memberProfileDTO = searchMemberRepository.findMemberProfile(myId, findId)
                .orElseThrow(() ->
                        new IllegalArgumentException("일치하는 회원이 없습니다")
                );

        return memberProfileDTO;
    }

    @Transactional(readOnly = true)
    public MyProfileDTO MyProfile(Long id) {

        //TODO : 없는 회원 찾을 때(ID 미스)
        //TODO : 사진이 없는 회원 찾을 때

        Object[] myProfile = memberRepository.findMyProfile(id);

        Member member = (Member) myProfile[0];
        MemberImg memberImg = (MemberImg) myProfile[1];

        MyProfileDTO myProfileDTO = null;

        if (memberImg != null) {
            myProfileDTO.setMemberImgDTO(new MemberImgDTO(memberImg.getFolderPath(), memberImg.getStoreFileName()));
        }
        myProfileDTO.setId(member.getId());
        myProfileDTO.setNickname(member.getNickname());
        myProfileDTO.setText(member.getText());
        myProfileDTO.setInstaurl(member.getInstaurl());
        myProfileDTO.setTwitterurl(member.getTwitterurl());

        return myProfileDTO;
    }


}
