package com.post_show_blues.vine.dto.auth;

import com.post_show_blues.vine.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@Builder
@ToString
public class SignupDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String nickname;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String university;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .nickname(nickname)
                .password(password)
                .email(email)
                .phone(phone)
                .university(university)
                .build();
    }
}
