package com.post_show_blues.vine.service.member;

import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.member.SearchMemberRepository;
import com.post_show_blues.vine.dto.member.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
        List<MemberListSQLDTO> members = memberRepository.findMemberByNickname(keyword);

        if (members.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<MemberListDTO> memberListDTO = members.stream().map(m -> {
            MemberListDTO memberList = new MemberListDTO(m.getMember_Id(), m.getNickname(), m.getText(), m.getFollowing(), m.getFollower());

            String folderPath = m.getFolder_Path();
            String storeFileName = m.getStore_File_Name();

            if (folderPath != null && storeFileName != null) {
                memberList.setMemberImgDTO(new MemberImgDTO(folderPath, storeFileName));
            }

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
