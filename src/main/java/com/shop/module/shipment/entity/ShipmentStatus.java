package com.shop.module.shipment.entity;

public enum ShipmentStatus {
    PREPARING("발송 준비 중"),
    PROCESSING("발송 처리 중"),
    SHIPPED("발송 완료"),
    DELIVERING("배달 중"),
    DELIVERED("배달 완료");

    private final String description;

    ShipmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
