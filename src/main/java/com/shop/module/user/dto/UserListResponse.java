package com.shop.module.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserListResponse {
    private List<UserDto> users;
    private int totalPages;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public UserListResponse(List<UserDto> users, int totalPages) {
        this.users = users;
        this.totalPages = totalPages;
    }
}
