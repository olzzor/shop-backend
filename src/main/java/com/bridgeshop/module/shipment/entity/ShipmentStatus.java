package com.bridgeshop.module.shipment.entity;

public enum ShipmentStatus {
    ACCEPTED,               // 배송 접수 (주문 접수)
    PREPARING,              // 배송 준비
    SHIPPING,               // 배송 중
    DELIVERED,              // 배송 완료
    CANCELED,               // 배송 취소 (주문 취소)
}
