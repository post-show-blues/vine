package com.post_show_blues.vine.config;

import com.post_show_blues.vine.config.auth.PrincipalDetailsService;
import com.post_show_blues.vine.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity //해당 파일로 시큐리티를 활성화
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) //@PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final PrincipalDetailsService principalDetailsService;

    @Bean
    public JwtFilter tokenAuthenticationFilter() {
        return new JwtFilter();
    }


    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //for h2-console
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        "/h2-console/**",
                        "/favicon.ico"
                );
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(principalDetailsService)
                .passwordEncoder(encode());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 X

                .and()
                .authorizeRequests()
                //먼저 정해진 값은 불변
                .antMatchers("/auth/**").permitAll() //회원가입, 로그인은 토큰이 없는 상태로 요청

                .antMatchers("/member/find/**", "/member",
                        "/meetings", "/meetings/**").permitAll()
                .antMatchers(HttpMethod.GET, "/meetings/**/requests").permitAll()
                .antMatchers(HttpMethod.GET, "/meetings/**/participants").permitAll()
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(tokenAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);

    }


}

