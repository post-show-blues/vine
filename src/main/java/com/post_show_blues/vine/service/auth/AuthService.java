package com.post_show_blues.vine.service.auth;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.file.FileStore;
import com.post_show_blues.vine.file.ResultFileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

//회원가입
@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberImgRepository memberImgRepository;
    private final FileStore fileStore;

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
    public Object[] join(SignupDto signupDto) throws IOException {
        //회원 정보
        Member member = signupDto.toMemberEntity();
        String rawPassword = member.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.setPassword(encPassword);
        Member memberEntity = memberRepository.save(member);

        MemberImg memberImgEntity = null;
        if (signupDto.getFile() != null) {
            ResultFileStore resultFileStore = fileStore.storeProfileFile(signupDto.getFile());
            MemberImg memberImg = signupDto.toMemberImgEntity(resultFileStore.getFolderPath(), resultFileStore.getStoreFileName(), member);
            memberImgEntity = memberImgRepository.save(memberImg);
        }

        return new Object[]{memberEntity, memberImgEntity};
    }

    /**
     * 전체 회원 조회
     */
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    private void validateDuplicateMember(String nickname) {
        Member findMember = memberRepository.findByNickname(nickname);
        if (findMember != null) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
