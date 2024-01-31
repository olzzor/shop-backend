package com.bridgeshop.module.shipment.entity;

public enum ShipmentStatus {
    ACCEPTED("주문 접수"),
    PREPARING("배송 준비"),
    SHIPPING("배송 중"),
    DELIVERED("배송 완료"),
    CANCELED("주문 취소");

    private final String description;

    ShipmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
