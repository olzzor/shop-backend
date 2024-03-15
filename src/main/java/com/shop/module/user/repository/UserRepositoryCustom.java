package com.shop.module.user.repository;

import com.shop.module.user.dto.UserListSearchRequest;
import com.shop.module.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> findByCondition(UserListSearchRequest userListSearchRequest, Pageable pageable);
}