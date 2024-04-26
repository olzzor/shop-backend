package com.shop.module.coupon.entity;

public enum CouponDiscountType {
    PERCENTAGE_DISCOUNT("비율 할인"),
    AMOUNT_DISCOUNT("금액 할인");

    private final String description;

    CouponDiscountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
