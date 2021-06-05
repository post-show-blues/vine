package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//회원가입
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 중복 닉네임 검증
     */
    @Transactional
    public void isDuplicateNickname(String nickname) {
        validateDuplicateMember(nickname);
    }

    /**
     * 회원가입
     */
    @Transactional
    public Member join(Member member) {
        String rawPassword = member.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.setPassword(encPassword);
        Member memberEntity = memberRepository.save(member);
        return memberEntity;
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    private void validateDuplicateMember(String nickname) {
        Member findMember = memberRepository.findByNickname(nickname);
        if (findMember!=null) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


}
