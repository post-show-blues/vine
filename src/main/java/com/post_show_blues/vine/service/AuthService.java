package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.dto.auth.SignupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

//회원가입
@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberImgRepository memberImgRepository;

    @Value("${org.zerock.upload.path}")
    private String uploadFolder;

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
    public Object[] join(SignupDto signupDto) {
        //회원 정보
        System.out.println("조인 메서드 실행");
        Member member = signupDto.toMemberEntity();
        String rawPassword = member.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.setPassword(encPassword);
        Member memberEntity = memberRepository.save(member);
        MemberImg memberImg;

        if (signupDto.getFile()==null) {
            memberImg=MemberImg.builder()
                    .member(member)
                    .build();
        } else {
            //회원 이미지
            UUID uuid = UUID.randomUUID(); //이미지 고유성 보장
            String imageFileName = uuid + "_" + signupDto.getFile().getOriginalFilename();
            Path imageFilePath = Paths.get(uploadFolder + "/" + imageFileName);

            try {
                Files.write(imageFilePath, signupDto.getFile().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            memberImg = signupDto.toMemberImgEntity(imageFileName, member);
        }

        MemberImg memberImgEntity = memberImgRepository.save(memberImg);

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
