package com.post_show_blues.vine.dto.auth;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SigninDto {
    @NotNull
    private String email;

    @NotNull
//    @Size(min=?, max=?) //TODO : DTO에 SIZE 추가
    private String password;
}
