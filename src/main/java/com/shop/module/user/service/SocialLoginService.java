package com.shop.module.user.service;

import com.shop.integration.feign.common.SocialAuthResponse;
import com.shop.integration.feign.common.SocialUserResponse;
import com.shop.module.user.entity.AuthProvider;
import org.springframework.stereotype.Service;

@Service
public interface SocialLoginService {
    AuthProvider getServiceName();

    SocialAuthResponse getAccessToken(String authorizationCode);

    SocialUserResponse getUserInfo(String accessToken);
}
