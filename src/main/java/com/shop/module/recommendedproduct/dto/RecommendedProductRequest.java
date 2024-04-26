package com.shop.module.recommendedproduct.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RecommendedProductRequest {
    private Long id;
    private Long userId;
    private Long productId;
    private String viewedAt;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public RecommendedProductRequest(Long id, Long userId, Long productId, String viewedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.viewedAt = viewedAt;
    }
}
