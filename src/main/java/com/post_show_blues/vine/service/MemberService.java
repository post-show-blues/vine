package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

//회원 정보 수정
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member memberUpdate(Long id, Member member){
        // 1. 영속화
        Member memberEntity = memberRepository.findById(id).orElseThrow(() ->{
                return new IllegalArgumentException("찾을 수 없는 id입니다");
        });

        memberEntity.setInstaurl(member.getInstaurl());
        memberEntity.setTwitterurl(member.getTwitterurl());
        memberEntity.setText(member.getText());

        return memberEntity;
    }
}
