package com.bridgeshop.module.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSizeUpsertRequest {
    private Long id;
    private String size;
    private int quantity;
    private int adjustmentQuantity;
}
