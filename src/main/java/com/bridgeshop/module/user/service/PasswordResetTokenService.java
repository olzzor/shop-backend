package com.bridgeshop.module.user.service;

import com.bridgeshop.common.exception.NotFoundException;
import com.bridgeshop.module.user.entity.PasswordResetToken;
import com.bridgeshop.module.user.entity.User;
import com.bridgeshop.module.user.repository.PasswordResetTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetToken retrieveByToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("passwordResetTokenNotFound", "패스워드 재설정 토큰 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public void addPasswordResetToken(String token, User user) {
        // 기존 토큰 삭제 (존재하는 경우에만 실행됨)
        deletePasswordResetToken(user.getId());

        // 새 토큰 생성 및 저장
        passwordResetTokenRepository.save(PasswordResetToken.builder()
                .user(user)
                .token(token)
                .expDate(LocalDateTime.now().plusDays(1))
                .build());
    }

    @Transactional
    public void deletePasswordResetToken(Long userId) {
        passwordResetTokenRepository.deleteByUserId(userId);
    }

    /**
     * 토큰의 유효성 검증
     *
     * @param token 검증할 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> prtOptional = passwordResetTokenRepository.findByToken(token);

        return prtOptional.isPresent()
                && prtOptional.get().getExpDate().isAfter(LocalDateTime.now());
    }
}