package com.bridgeshop.module.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PasswordResetRequest {
    private String token;
    private String password;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public PasswordResetRequest(String token, String password) {
        this.token = token;
        this.password = password;
    }
}