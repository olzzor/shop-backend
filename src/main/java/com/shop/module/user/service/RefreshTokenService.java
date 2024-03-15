package com.shop.module.user.service;

import com.shop.module.user.entity.RefreshToken;
import com.shop.module.user.entity.User;
import com.shop.module.user.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void addRefreshToken(String token, User user) {
        LocalDateTime expDate = LocalDateTime.now().plusDays(7);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expDate(expDate)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}