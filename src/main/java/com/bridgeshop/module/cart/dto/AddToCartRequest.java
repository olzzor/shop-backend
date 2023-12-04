package com.bridgeshop.module.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    private Long productSizeId;
    private Long favoriteId;
    private int quantity;
}
