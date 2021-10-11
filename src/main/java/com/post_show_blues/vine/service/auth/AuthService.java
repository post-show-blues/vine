package com.post_show_blues.vine.service.auth;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.dto.auth.SigninDto;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.dto.auth.SignupResponse;
import com.post_show_blues.vine.exception.AlreadyExistedEmailException;
import com.post_show_blues.vine.exception.AlreadyExistedNicknameException;
import com.post_show_blues.vine.file.FileStore;
import com.post_show_blues.vine.file.ResultFileStore;
import com.post_show_blues.vine.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

//회원가입
@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberImgRepository memberImgRepository;
    private final FileStore fileStore;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Transactional
    public void isDuplicateNickname(String nickname){
        validateDuplicateNickname(nickname);
    }

    @Transactional
    public void isDuplicateEmail(String email){
        validateDuplicateEmail(email);
    }

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponse join(SignupDto signupDto) throws IOException {
        //회원 정보
        Member member = signupDto.toMemberEntity();

        isDuplicateNickname(member.getNickname());
        isDuplicateEmail(member.getEmail());

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        Member memberEntity = memberRepository.save(member);

        MemberImg memberImgEntity = null;
        if (signupDto.getFile() != null) {
            ResultFileStore resultFileStore = fileStore.storeProfileFile(signupDto.getFile());
            MemberImg memberImg = signupDto.toMemberImgEntity(resultFileStore.getFolderPath(), resultFileStore.getStoreFileName(), member);
            memberImgEntity = memberImgRepository.save(memberImg);
        }



        return new SignupResponse(memberEntity, memberImgEntity);
    }

    /**
     * 로그인
     */
    public String login(SigninDto signinDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinDto.getEmail(),
                        signinDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.createToken(authentication);
    }


    /**
     * 전체 회원 조회
     */
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    private void validateDuplicateNickname(String nickname){
        Member findMember = memberRepository.findByNickname(nickname).orElse(null);
        if(findMember!=null){
            throw new AlreadyExistedNicknameException("이미 사용중인 닉네임입니다.");
        }
    }

    private void validateDuplicateEmail(String email){
        Member findMember = memberRepository.findByEmail(email).orElse(null);
        if(findMember!=null){
            throw new AlreadyExistedEmailException("이미 사용중인 이메일입니다.");
        }
    }

}
