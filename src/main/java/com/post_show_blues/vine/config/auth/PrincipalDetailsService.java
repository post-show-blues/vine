package com.post_show_blues.vine.config.auth;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service //IoC //로그인 요청하면 실행
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //1. 패스워드는 알아서 체킹하니까 신경 x
    //2. 리턴이 잘 되면 자동으로 UserDetails타입 세션 만듦

   @Override
   @Transactional
    public PrincipalDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Member member = memberRepository.findByEmail(email).orElseThrow(() ->
               new UsernameNotFoundException("이메일" + email + "을 가진 사용자가 존재하지 않습니다."));
       return new PrincipalDetails(member);
    }

    @Transactional
    public PrincipalDetails loadUserById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("id "+id+"를 가진 사용자가 없습니다.")
        );

        return new PrincipalDetails(member);
    }
}
