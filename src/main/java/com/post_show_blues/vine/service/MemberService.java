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

import java.util.Arrays;
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

            MemberListDTO memberList = new MemberListDTO();

            if (memberImg != null) {
                memberList.setMemberImgDTO(new MemberImgDTO(memberImg.getFolderPath(), memberImg.getStoreFileName()));
            }
            memberList.setId(member.getId());
            memberList.setNickname(member.getNickname());
            memberList.setText(member.getText());

            return memberList;
        }).collect(Collectors.toList());

        return memberListDTO;
    }

    @Transactional(readOnly = true)
    public MemberProfileDTO memberProfile(Long myId, Long findId) {

        MemberProfileDTO memberProfileDTO = searchMemberRepository.findMemberProfile(myId, findId)
                .orElseThrow(() ->
                        new IllegalArgumentException("일치하는 회원이 없습니다")
                );

        return memberProfileDTO;
    }

    @Transactional(readOnly = true)
    public MyProfileDTO MyProfile(Long id) {

        MyProfileDTO myProfile = searchMemberRepository.findMyProfile(id);

        return myProfile;
    }


}
