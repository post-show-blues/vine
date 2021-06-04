package com.post_show_blues.vine.config;

import com.post_show_blues.vine.dto.Auth.SignupDto;
import com.post_show_blues.vine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity //해당 파일로 시큐리티를 활성화
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encode(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //기존 시큐리티가 가진 기능 비활성화
        super.configure(http); //인증되지 않은 사용자 /login 페이지로 가로채짐(리다이렉트)

        //CSRF 토큰 : 정상적인 사용자인지 아닌지 판단해줌
        http.csrf().disable();

//        http.authorizeRequests()
//
//                //antMatchers() 안의 경로들은 인증이 필요하다
//                .antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/coments/**").authenticated()
//
//                //다른 request 들은 모두 허용한다
//                .anyRequest().permitAll()
//                .and()
//
//                //인증이 필요한 페이지는 loginPage로 리다이렉션(302) 된다
//                .formLogin()
//                .loginPage("/auth/signin") //GET
//
//                .loginProcessingUrl("/auth/signin") //POST, 로그인 요청 낚아챔
//
//                //성공하면 / 페이지로 가진다
//                .defaultSuccessUrl("/");
    }


}

