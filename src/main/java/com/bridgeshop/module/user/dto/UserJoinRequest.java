package com.bridgeshop.module.user.dto;

import com.bridgeshop.module.user.entity.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {
    private String name;
    private String email;
    private String password;            // 회원가입 전용 (소셜 로그인 제외)
    private String passwordConfirm;     // 회원가입 전용 (소셜 로그인 제외)
    private AuthProvider authProvider;
    private String socialId;
}
