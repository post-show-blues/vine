package com.post_show_blues.vine.config.auth;

import com.post_show_blues.vine.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Getter
@Setter
public class PrincipalDetails implements UserDetails {

    private static final String ROLE_USER = "ROLE_USER";

    private Member member;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    public PrincipalDetails(Member member, Collection<? extends GrantedAuthority> authorities) {
        this.member = member;
        this.authorities = authorities;
    }


    public static PrincipalDetails create(Member member) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(ROLE_USER));

        return new PrincipalDetails(member, authorities);
    }

    public static PrincipalDetails create(Member member, Map<String, Object> attributes) {
        PrincipalDetails userPrincipal = PrincipalDetails.create(member);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public Long getId(){
        return member.getId();
    }

    public String getEmail(){
        return member.getEmail();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
