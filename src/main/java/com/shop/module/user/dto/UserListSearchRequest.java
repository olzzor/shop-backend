package com.shop.module.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserListSearchRequest {
    private String id;
    private String authProvider;
    private String email;
    private String name;
    private String phoneNumber;
    @JsonProperty("isAdmin")
    private String isAdmin;
    @JsonProperty("isActivate")
    private String isActivate;
    private String startRegDate;
    private String endRegDate;
    private String startModDate;
    private String endModDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public UserListSearchRequest(String id, String authProvider, String email, String name,
                                 String phoneNumber, String isAdmin, String isActivate,
                                 String startRegDate, String endRegDate, String startModDate, String endModDate) {
        this.id = id;
        this.authProvider = authProvider;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
        this.isActivate = isActivate;
        this.startRegDate = startRegDate;
        this.endRegDate = endRegDate;
        this.startModDate = startModDate;
        this.endModDate = endModDate;
    }
}
