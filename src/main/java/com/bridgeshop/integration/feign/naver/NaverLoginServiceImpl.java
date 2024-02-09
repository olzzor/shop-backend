package com.bridgeshop.integration.feign.naver;

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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("naverLogin")
public class NaverLoginServiceImpl implements SocialLoginService {

    private final NaverAuthApi naverAuthApi;
    private final NaverUserApi naverUserApi;

    @Value("${social.client.naver.client-id}")
    private String naverAppKey;
    @Value("${social.client.naver.client-secret}")
    private String naverAppSecret;
    @Value("${social.client.naver.authorization-grant-type}")
    private String naverGrantType;


    @Override
    public AuthProvider getServiceName() {
        return AuthProvider.NAVER;
    }

    @Override
    public SocialAuthResponse getAccessToken(String authorizationCode) {
        ResponseEntity<?> response = naverAuthApi.getAccessToken(
                naverGrantType,
                naverAppKey,
                naverAppSecret,
                authorizationCode,
                "state"
        );

        log.info("naver auth response {}", response.toString());

        return new Gson()
                .fromJson(
                        String.valueOf(response.getBody())
                        , SocialAuthResponse.class
                );
    }

    @Override
    public SocialUserResponse getUserInfo(String accessToken) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("authorization", "Bearer " + accessToken);

        ResponseEntity<?> response = naverUserApi.getUserInfo(headerMap);

        log.info("naver user response");
        log.info(response.toString());

        String jsonString = response.getBody().toString();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
                .create();

        NaverLoginResponse naverLoginResponse = gson.fromJson(jsonString, NaverLoginResponse.class);
        NaverLoginResponse.Response naverUserInfo = naverLoginResponse.getResponse();

        return SocialUserResponse.builder()
                .id(naverUserInfo.getId())
                .name(naverUserInfo.getName())
                .email(naverUserInfo.getEmail())
                .build();
    }
}
