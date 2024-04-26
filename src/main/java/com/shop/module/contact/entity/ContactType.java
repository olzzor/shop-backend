package com.shop.module.contact.entity;

public enum ContactType {
    SHIPPING("배송"),
    ORDER_PAYMENT("주문/결제"),
    PRODUCT_INFO("상품 정보"),
    EXCHANGE_RETURN_REFUND("교환/반품/환불"),
    PRICE_PROMOTION("가격/프로모션"),
    PRIVACY("개인 정보"),
    PARTNERSHIP("제휴/협력"),
    OTHER("기타");

    private final String description;

    ContactType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
