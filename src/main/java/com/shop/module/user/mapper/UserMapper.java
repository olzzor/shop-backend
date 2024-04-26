package com.shop.module.user.mapper;

import com.shop.module.user.dto.UserDto;
import com.shop.module.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .authProvider(user.getAuthProvider())
                .isAdmin(user.isAdmin())
                .isActivate(user.isActivate())
                .regDate(user.getRegDate())
                .modDate(user.getModDate())
                .build();
    }

    public List<UserDto> mapToDtoList(List<User> userList) {
        return userList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}