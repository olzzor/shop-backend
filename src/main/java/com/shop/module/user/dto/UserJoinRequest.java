package com.shop.module.user.dto;

import com.shop.module.user.entity.AuthProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserJoinRequest {
    private String name;
    private String email;
    private String password;            // 회원가입 전용 (소셜 로그인 제외)
    private String passwordConfirm;     // 회원가입 전용 (소셜 로그인 제외)
    private AuthProvider authProvider;
    private String socialId;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public UserJoinRequest(String name, String email, String password, String passwordConfirm,
                           AuthProvider authProvider, String socialId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.authProvider = authProvider;
        this.socialId = socialId;
    }
}
