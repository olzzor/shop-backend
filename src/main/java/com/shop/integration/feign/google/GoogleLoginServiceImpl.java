package com.shop.integration.feign.google;

import com.shop.integration.feign.common.SocialAuthResponse;
import com.shop.integration.feign.common.SocialUserResponse;
import com.shop.module.user.entity.AuthProvider;
import com.shop.common.util.GsonLocalDateTimeAdapter;
import com.shop.module.user.service.SocialLoginService;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("googleLogin")
public class GoogleLoginServiceImpl implements SocialLoginService {
    private final GoogleAuthApi googleAuthApi;
    private final GoogleUserApi googleUserApi;

    @Value("${social.client.google.client-id}")
    private String googleClientId;
    @Value("${social.client.google.client-secret}")
    private String googleClientSecret;
    @Value("${social.client.google.redirect-uri}")
    private String googleRedirectUri;
    @Value("${social.client.google.authorization-grant-type}")
    private String googleAuthorizationGrantType;

    @Override
    public AuthProvider getServiceName() {
        return AuthProvider.GOOGLE;
    }

    @Override
    public SocialAuthResponse getAccessToken(String authorizationCode) {
        ResponseEntity<?> response = googleAuthApi.getAccessToken(
                GoogleRequestAccessTokenDto.builder()
                        .code(authorizationCode)
                        .client_id(googleClientId)
                        .clientSecret(googleClientSecret)
                        .redirect_uri(googleRedirectUri)
                        .grant_type(googleAuthorizationGrantType)
                        .build()
        );

        log.info("google auth info");
        log.info(response.toString());

        return new Gson()
                .fromJson(
                        response.getBody().toString(),
                        SocialAuthResponse.class
                );
    }

    @Override
    public SocialUserResponse getUserInfo(String accessToken) {
        ResponseEntity<?> response = googleUserApi.getUserInfo(accessToken);

        log.info("google user response");
        log.info(response.toString());

        String jsonString = response.getBody().toString();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
                .create();

        GoogleLoginResponse googleLoginResponse = gson.fromJson(jsonString, GoogleLoginResponse.class);

        return SocialUserResponse.builder()
                .id(googleLoginResponse.getId())
                .email(googleLoginResponse.getEmail())
                .build();
    }
}
