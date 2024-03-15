package com.shop.module.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LoginResponse {
    private Long id;
    private String role; // 추가
    private String authProvider; // 추가

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public LoginResponse(Long id, String role, String authProvider) {
        this.id = id;
        this.role = role;
        this.authProvider = authProvider;
    }
}
