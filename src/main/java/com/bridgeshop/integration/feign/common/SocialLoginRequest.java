package com.bridgeshop.integration.feign.common;

import com.bridgeshop.module.user.entity.AuthProvider;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SocialLoginRequest {
    @NotNull
    private AuthProvider authProvider;
    @NotNull
    private String code;
}
