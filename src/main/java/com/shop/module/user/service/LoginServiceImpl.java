package com.shop.module.user.service;

import com.shop.integration.feign.common.SocialAuthResponse;
import com.shop.integration.feign.common.SocialUserResponse;
import com.shop.module.user.entity.AuthProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
//@Service("defaultLoginService")
@Service
@Component
@Qualifier("defaultLoginService")
public class LoginServiceImpl implements SocialLoginService {
    @Override
    public AuthProvider getServiceName() {
        return AuthProvider.LOCAL;
    }

    @Override
    public SocialAuthResponse getAccessToken(String authorizationCode) {
        return null;
    }

    @Override
    public SocialUserResponse getUserInfo(String accessToken) {
        return null;
    }
}
