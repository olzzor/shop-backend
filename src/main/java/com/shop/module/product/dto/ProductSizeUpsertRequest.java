package com.shop.module.product.dto;

import lombok.Getter;

@Getter
public class ProductSizeUpsertRequest {
    private Long id;
    private String size;
    private int quantity;
    private int adjustmentQuantity;
}
