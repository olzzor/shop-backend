package com.bridgeshop.module.cart.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartProductUpdateRequest {
    private Long id;
    private Long cartId;
    private Long productId;
    private int quantity;
    private Long couponId;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CartProductUpdateRequest(Long id, Long cartId, Long productId, int quantity, Long couponId) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
        this.couponId = couponId;
    }
}

