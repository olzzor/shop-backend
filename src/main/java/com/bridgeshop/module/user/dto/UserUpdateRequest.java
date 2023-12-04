package com.bridgeshop.module.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirm;
}
