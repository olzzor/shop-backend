package com.shop.module.coupon.entity;

public enum CouponType {
    PERCENTAGE_DISCOUNT("비율 할인"),
    AMOUNT_DISCOUNT("금액 할인"),
    SIGN_UP_DISCOUNT("신규 할인"),
    USER_DISCOUNT("특정 고객 할인"),
    FREE_SHIPPING("무료 배송"),
    SEASONAL_EVENT_COUPON("시즌 이벤트");

    private final String description;

    CouponType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
