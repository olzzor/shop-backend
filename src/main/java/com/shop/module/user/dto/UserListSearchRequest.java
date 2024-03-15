package com.shop.module.user.dto;

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
    private String adminFlag;
    private String activateFlag;
    private String startRegDate;
    private String endRegDate;
    private String startModDate;
    private String endModDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public UserListSearchRequest(String id, String authProvider, String email, String name,
                                 String phoneNumber, String adminFlag, String activateFlag,
                                 String startRegDate, String endRegDate, String startModDate, String endModDate) {
        this.id = id;
        this.authProvider = authProvider;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.adminFlag = adminFlag;
        this.activateFlag = activateFlag;
        this.startRegDate = startRegDate;
        this.endRegDate = endRegDate;
        this.startModDate = startModDate;
        this.endModDate = endModDate;
    }
}
