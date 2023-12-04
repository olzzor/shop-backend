package com.bridgeshop.module.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
