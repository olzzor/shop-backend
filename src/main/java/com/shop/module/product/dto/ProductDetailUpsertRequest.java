package com.shop.module.product.dto;

import lombok.Getter;

@Getter
public class ProductDetailUpsertRequest {
    private Long id;
    private String description;
    private String content;
}
