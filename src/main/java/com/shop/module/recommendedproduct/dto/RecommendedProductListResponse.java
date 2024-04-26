package com.shop.module.recommendedproduct.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RecommendedProductListResponse {
    private List<RecommendedProductDto> recommendedProducts;
    private int totalPages;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public RecommendedProductListResponse(List<RecommendedProductDto> recommendedProducts, int totalPages) {
        this.recommendedProducts = recommendedProducts;
        this.totalPages = totalPages;
    }
}
