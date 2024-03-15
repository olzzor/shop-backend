package com.shop.module.user.entity;

public enum AuthProvider {
    LOCAL("local"),
    GOOGLE("google"),
    FACEBOOK("facebook"),
    NAVER("naver"),
    KAKAO("kakao");

    private final String description;

    AuthProvider(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
