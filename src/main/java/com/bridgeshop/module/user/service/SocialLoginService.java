package com.bridgeshop.module.user.service;

import com.bridgeshop.integration.feign.common.SocialAuthResponse;
import com.bridgeshop.integration.feign.common.SocialUserResponse;
import com.bridgeshop.module.user.entity.AuthProvider;
import org.springframework.stereotype.Service;

@Service
public interface SocialLoginService {
    AuthProvider getServiceName();

    SocialAuthResponse getAccessToken(String authorizationCode);

    SocialUserResponse getUserInfo(String accessToken);
}
