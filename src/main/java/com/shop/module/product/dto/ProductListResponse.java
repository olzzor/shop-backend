package com.shop.module.product.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductListResponse {
    private List<ProductDto> products;
    private int totalPages;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ProductListResponse(List<ProductDto> products, int totalPages) {
        this.products = products;
        this.totalPages = totalPages;
    }
}
