package com.bridgeshop.module.user.repository;

import com.bridgeshop.module.user.dto.UserListSearchRequest;
import com.bridgeshop.module.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> findByCondition(UserListSearchRequest userListSearchRequest, Pageable pageable);
}