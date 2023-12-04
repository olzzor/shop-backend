package com.bridgeshop.module.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartProductUpdateRequest {
    private Long id;
    private Long cartId;
    private Long productId;
    private int quantity;
    private Long couponId;
}

