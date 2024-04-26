package com.shop.module.product.entity;

public enum ProductStatus {
    ON_SALE("판매 중"),
    TEMP_OUT_OF_STOCK("일시 품절"),
    OUT_OF_STOCK("품절");

    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
