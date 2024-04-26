package com.shop.module.recommendedproduct.dto;

import lombok.Getter;

@Getter
public class RecommendedProductUpsertRequest {
    private Long id;
    private Long productId;
    private int displayOrder;
}
