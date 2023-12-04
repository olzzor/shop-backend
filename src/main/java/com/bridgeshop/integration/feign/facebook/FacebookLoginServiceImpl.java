package com.bridgeshop.integration.feign.facebook;

import com.bridgeshop.integration.feign.common.SocialAuthResponse;
import com.bridgeshop.integration.feign.common.SocialUserResponse;
import com.bridgeshop.module.user.entity.AuthProvider;
import com.bridgeshop.common.util.GsonLocalDateTimeAdapter;
import com.bridgeshop.module.user.service.SocialLoginService;
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
@Qualifier("facebookLogin")
public class FacebookLoginServiceImpl implements SocialLoginService {
    private final FacebookAuthApi facebookAuthApi;
    private final FacebookUserApi facebookUserApi;

    @Value("${social.client.facebook.client-id}")
    private String facebookClientId;
    @Value("${social.client.facebook.client-secret}")
    private String facebookClientSecret;
    @Value("${social.client.facebook.redirect-uri}")
    private String facebookRedirectUri;
    @Value("${social.client.facebook.authorization-grant-type}")
    private String facebookAuthorizationGrantType;

    @Override
    public AuthProvider getServiceName() {
        return AuthProvider.FACEBOOK;
    }

    @Override
    public SocialAuthResponse getAccessToken(String authorizationCode) {
        ResponseEntity<?> response = facebookAuthApi.getAccessToken(
                FacebookRequestAccessTokenDto.builder()
                        .code(authorizationCode)
                        .client_id(facebookClientId)
                        .client_secret(facebookClientSecret)
                        .redirect_uri(facebookRedirectUri)
                        .grant_type(facebookAuthorizationGrantType)
                        .build()
        );

        log.info("facebook auth info");
        log.info(response.toString());

        return new Gson()
                .fromJson(
                        response.getBody().toString(),
                        SocialAuthResponse.class
                );
    }

    @Override
    public SocialUserResponse getUserInfo(String accessToken) {
        ResponseEntity<?> response = facebookUserApi.getUserInfo(accessToken, "id,name,email");

        log.info("facebook user response");
        log.info(response.toString());

        String jsonString = response.getBody().toString();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
                .create();

        FacebookLoginResponse facebookLoginResponse = gson.fromJson(jsonString, FacebookLoginResponse.class);

        return SocialUserResponse.builder()
                .id(facebookLoginResponse.getId())
                .name(facebookLoginResponse.getName())
                .email(facebookLoginResponse.getEmail())
                .build();
    }
}
