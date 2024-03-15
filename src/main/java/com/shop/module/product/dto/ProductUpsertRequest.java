package com.shop.module.product.dto;

import com.shop.module.product.entity.ProductStatus;
import lombok.Getter;

@Getter
public class ProductUpsertRequest {
    private Long id;
    private String categoryCode;
    private String code;
    private String name;
    private int price;
    private int discountPer;
    private String detail;
    private ProductStatus status;
}
