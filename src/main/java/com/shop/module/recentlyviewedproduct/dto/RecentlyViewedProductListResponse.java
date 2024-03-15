package com.shop.module.recentlyviewedproduct.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RecentlyViewedProductListResponse {
    private List<RecentlyViewedProductDto> recentlyViewedProducts;
    private int totalPages;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public RecentlyViewedProductListResponse(List<RecentlyViewedProductDto> recentlyViewedProducts, int totalPages) {
        this.recentlyViewedProducts = recentlyViewedProducts;
        this.totalPages = totalPages;
    }
}
