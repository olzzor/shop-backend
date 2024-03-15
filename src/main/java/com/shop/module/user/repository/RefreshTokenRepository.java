package com.shop.module.user.repository;

import com.shop.module.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUser_Id(Long userId);

    void deleteByUserId(Long userId);
}