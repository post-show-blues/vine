package com.post_show_blues.vine.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity //해당 파일로 시큐리티를 활성화
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super 삭제 - 기존 시큐리티가 가지고 있는 기능이 다 비활성화됨.
        //CSRF 토큰 : 정상적인 사용자인지 아닌지 판단해줌
        http.csrf().disable();
        http.authorizeRequests()
                //TODO : url 권한 설정
                //TODO : 로그인 실패시 화면 설정
                //TODO : /meetings/{meeintg-id}권한 문제
                //먼저 정해진 값은 불변
                .antMatchers("/meetings/new").authenticated()
                .antMatchers("/auth/signup", "/member/find/**", "/member",
                        "/meetings", "/meetings/**").permitAll()
                .antMatchers(HttpMethod.GET, "/meetings/**/requests").permitAll()
                .anyRequest().authenticated()
                .and()

                .formLogin()
                .loginPage("/auth/signin") // GET
                .loginProcessingUrl("/auth/signin") // POST -> 폼태그로 요청, 스프링 시큐리티가 로그인 프로세스 진행
                .usernameParameter("email")
                .defaultSuccessUrl("/meetings")
                .and()

                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                .logoutUrl("/auth/logout") //POST -> 폼태그로 요청
                .logoutSuccessUrl("/meetings")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
//                .and()
//                .oauth2Login() // form로그인도 하는데, oauth2로그인도 할꺼야!!
//                .userInfoEndpoint() // oauth2로그인을 하면 최종응답을 회원정보를 바로 받을 수 있다.
//                .userService(oAuth2DetailsService);
    }


}

