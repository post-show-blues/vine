package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberImgServiceImpl implements MemberImgService{

    private final MemberImgRepository memberImgRepository;

    /**
     * 프로필사진조회
     */
    @Transactional(readOnly = true)
    @Override
    public MemberImg findOne(Long memberImgId) {

        if(memberImgId == null){
            return null;
        }
        else{
            return memberImgRepository.findById(memberImgId).get();
        }
    }
}
