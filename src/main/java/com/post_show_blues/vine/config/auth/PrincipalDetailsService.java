package com.post_show_blues.vine.config.auth;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service //IoC //로그인 요청하면 실행
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //1. 패스워드는 알아서 체킹하니까 신경 x
    //2. 리턴이 잘 되면 자동으로 UserDetails타입 세션 만듦
   @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByEmail(username);

       System.out.println("memberEntity = " + memberEntity);

        if(memberEntity == null){
            return null;
        }else{
            return new PrincipalDetails(memberEntity);
        }
    }
}
