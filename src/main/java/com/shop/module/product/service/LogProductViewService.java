package com.shop.module.product.service;

import com.shop.module.product.entity.LogProductView;
import com.shop.module.product.repository.LogProductViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogProductViewService {

    private final LogProductViewRepository logProductViewRepository;

    /**
     * 상품 조회 로그 등록
     */
    public void createProductViewLog(Long userId, Long productId) {
        insertProductViewLog(userId, productId);
    }

    /**
     * 상품 조회 로그 등록 (DB)
     */
    @Transactional
    public void insertProductViewLog(Long userId, Long productId) {
        // 상품 조회 로그 내용 작성
        LogProductView logProductView = LogProductView.builder()
                .productId(productId)
                .userId(userId)
                .viewedAt(LocalDateTime.now())
                .build();

        // DB 등록
        logProductViewRepository.save(logProductView);
    }
}