package com.shop.module.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserUpdateRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirm;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public UserUpdateRequest(String name, String email, String phoneNumber,
                             String currentPassword, String newPassword, String newPasswordConfirm) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }
}
