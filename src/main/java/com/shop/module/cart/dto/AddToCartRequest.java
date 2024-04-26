package com.shop.module.cart.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AddToCartRequest {
    private Long productSizeId;
    private Long wishlistId;
    private int quantity;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public AddToCartRequest(Long productSizeId, Long wishlistId, int quantity) {
        this.productSizeId = productSizeId;
        this.wishlistId = wishlistId;
        this.quantity = quantity;
    }
}
